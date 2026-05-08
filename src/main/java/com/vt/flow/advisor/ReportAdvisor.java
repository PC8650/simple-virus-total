package com.vt.flow.advisor;

import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.advisor.utils.ErrorRespUtil;
import com.vt.flow.dto.ReportContent;
import com.vt.flow.enums.TypeEnum;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.remote.dto.VtResult;
import lombok.RequiredArgsConstructor;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.awaitility.core.ConditionTimeoutException;
import org.awaitility.pollinterval.IterativePollInterval;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 报告顾问。获取报告
 */
@Component
@RequiredArgsConstructor
public class ReportAdvisor implements CallAdvisor {

    private final Thread.Builder virtualThreadBuilder = Thread.ofVirtual().name("Report-", 0);

    private final ConditionFactory conditionFactory = Awaitility.given()
            .pollThread(virtualThreadBuilder::start)
            .pollInterval(
                    new IterativePollInterval((duration) -> duration.multipliedBy(2), Duration.ofSeconds(3))
            )
            .atMost(Duration.ofSeconds(300));

    public VtResult<?> getBehaviourReport(Scanner scanner, String reportId) {
        try {
            return conditionFactory.until(
                    () -> scanner.getBehaviourReport(reportId),
                    (r) -> !r.isSuccess() || !r.getMeta().containsKey("sandboxes_in_progress")
            );
        }catch (ConditionTimeoutException e) {
            VtResult<?> result = new VtResult<>();
            result.setError("Behaviour Analyse Timeout");
            return result;
        }
    }

    @NotNull
    @Override
    public ChatClientResponse adviseCall(@NotNull ChatClientRequest chatClientRequest, @NotNull CallAdvisorChain callAdvisorChain) {
        Scanner scanner = ChainKey.SCANNER.get(chatClientRequest);
        String reportId = scanner.getReportId(chatClientRequest);

        //获取报告
        VtResult<?> report = scanner.getReport(reportId);
        if (!report.isSuccess()) {
            return ErrorRespUtil.buildErrorResp(chatClientRequest, "Report fetch failed: " + report.getError());
        }

        //获取行为报告
        TypeEnum type = scanner.type();
        Object behaviourReportData = null;
        if (TypeEnum.FILE.equals(type)) {
            VtResult<?> behaviourReport = getBehaviourReport(scanner, reportId);
            if (!behaviourReport.isSuccess()) {
                return ErrorRespUtil.buildErrorResp(chatClientRequest, "Behaviour Report fetch failed: " + report.getError());
            }
            behaviourReportData = behaviourReport.getData();
        }

        ReportContent reportContent = new ReportContent()
                .setType(type.name())
                .setUrl(type.guiUrl(reportId))
                .setReport(report.getData())
                .setBehaviour(behaviourReportData);

        ChainKey.REPORT_SUMMARY.put(chatClientRequest, reportContent);

        return callAdvisorChain.nextCall(chatClientRequest);
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
