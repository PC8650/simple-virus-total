package com.vt.flow.advisor;

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
import com.vt.enums.MsgEnum;
import com.vt.utils.MessageUtils;

/**
 * 报告顾问。获取报告
 */
@Component
@RequiredArgsConstructor
public class ReportAdvisor implements StreamAdvisor {

    private final String metaKey = "sandboxes_in_progress";

    public VtResult<?> getBehaviourReport(Scanner scanner, String reportId, ChatClientRequest chatClientRequest) {
        com.vt.flow.dto.InputContent inputContent = ChainKey.INPUT.get(chatClientRequest);
        String lang = inputContent.getLanguage();

        FlowSseUtil.sendNotMainText(chatClientRequest, getName(),
                MessageUtils.getMessage(lang, MsgEnum.SSE_REPORT_SANDBOX_START, reportId));
        return PollUtil.poll(600000L, 30000L,
                () -> {
                    VtResult<?> behaviourReport = scanner.getBehaviourReport(reportId);
                    String process = behaviourReport.getMeta().getOrDefault(metaKey, "[]").toString();
                    FlowSseUtil.sendNotMainText(chatClientRequest, getName(),
                            MessageUtils.getMessage(lang, MsgEnum.SSE_REPORT_SANDBOX_POLLING, process));
                    return behaviourReport;
                },
                (r) -> !r.getMeta().containsKey(metaKey));
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Flux<ChatClientResponse> adviseStream(@NotNull ChatClientRequest chatClientRequest, @NotNull StreamAdvisorChain streamAdvisorChain) {
        ChainKey.CURRENT.get(chatClientRequest).set(getName());
        Scanner scanner = ChainKey.SCANNER.get(chatClientRequest);
        String reportId = ChainKey.CACHE.get(chatClientRequest).getReportId();

        // 获取报告
        String lang = ChainKey.INPUT.get(chatClientRequest).getLanguage();
        FlowSseUtil.sendNotMainText(chatClientRequest, getName(),
                MessageUtils.getMessage(lang, MsgEnum.SSE_REPORT_FETCH, reportId));
        VtResult<?> report = scanner.getReport(reportId);

        // 获取行为报告
        TypeEnum type = scanner.type();
        Object behaviourReportData = null;
        Object behaviourMitreData = null;
        if (TypeEnum.FILE.equals(type)) {
            VtResult<?> behaviourReport = getBehaviourReport(scanner, reportId, chatClientRequest);
            behaviourReportData = behaviourReport.getData();
            // 获取 战术/技术 汇总
            FlowSseUtil.sendNotMainText(chatClientRequest, getName(),
                    MessageUtils.getMessage(lang, MsgEnum.SSE_REPORT_MITRE_START, reportId));
            VtResult<?> behaviourMitre = scanner.getBehaviourMitre(reportId);
            behaviourMitreData = behaviourMitre.getData();
        }

        FlowSseUtil.sendNotMainText(chatClientRequest, getName(),
                MessageUtils.getMessage(lang, MsgEnum.SSE_REPORT_SUMMARY, reportId));

        ReportContent reportContent = new ReportContent()
                .setType(type)
                .setUrl(type.guiUrl(reportId))
                .setReport(report.getData())
                .setBehaviour(behaviourReportData)
                .setMitre(behaviourMitreData);

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
