package com.vt.flow.advisor;

import com.vt.exception.WrapperException;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.utils.FlowSseUtil;
import com.vt.flow.dto.ReportContent;
import com.vt.flow.enums.TypeEnum;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.flow.utils.PollUtil;
import com.vt.remote.dto.VtResult;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 报告顾问。获取报告
 */
@Component
@RequiredArgsConstructor
public class ReportAdvisor implements StreamAdvisor {

    public VtResult<?> getBehaviourReport(Scanner scanner, String reportId, ChatClientRequest chatClientRequest) {
        AtomicReference<String> process = new AtomicReference<>("");
        return PollUtil.poll(600000L, 15000L,
                () -> {
                    FlowSseUtil.sendNotMainText(chatClientRequest, getName(), "轮询沙箱行为分析状态 (15s 间隔, 上限10m)... ID: " + reportId + " PROCESS: " + process.get());
                    VtResult<?> behaviourReport = scanner.getBehaviourReport(reportId);
                    if (behaviourReport.isSuccess()) {
                        process.set(behaviourReport.getMeta().getOrDefault("sandboxes_in_progress", "").toString());
                    }
                    return behaviourReport;
                },
                (r) -> !r.isSuccess() || !r.getMeta().containsKey("sandboxes_in_progress")
        );
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Flux<ChatClientResponse> adviseStream(@NotNull ChatClientRequest chatClientRequest, @NotNull StreamAdvisorChain streamAdvisorChain) {
        ChainKey.CURRENT.get(chatClientRequest).set(getName());
        Scanner scanner = ChainKey.SCANNER.get(chatClientRequest);
        String reportId = scanner.getReportId(chatClientRequest);

        // 获取报告
        FlowSseUtil.sendNotMainText(chatClientRequest, getName(), "拉取分析报告... ID: " + reportId);
        VtResult<?> report = scanner.getReport(reportId);
        if (!report.isSuccess()) {
            throw new WrapperException("Report fetch failed: " + report.getError());
        }

        // 获取行为报告
        TypeEnum type = scanner.type();
        Object behaviourReportData = null;
        if (TypeEnum.FILE.equals(type)) {
            VtResult<?> behaviourReport = getBehaviourReport(scanner, reportId, chatClientRequest);
            if (!behaviourReport.isSuccess())
                throw new WrapperException("Behaviour Report fetch failed: " + report.getError());
            behaviourReportData = behaviourReport.getData();
        }

        FlowSseUtil.sendNotMainText(chatClientRequest, getName(), "汇总报告数据 " + reportId);

        ReportContent reportContent = new ReportContent()
                .setType(type)
                .setUrl(type.guiUrl(reportId))
                .setReport(report.getData())
                .setBehaviour(behaviourReportData);

        ChainKey.REPORT_SUMMARY.put(chatClientRequest, reportContent);

        return streamAdvisorChain.nextStream(chatClientRequest);
    }

    @NotNull
    @Override
    public String getName() {
        return "ReportAdvisor";
    }

    @Override
    public int getOrder() {
        return 20;
    }

}
