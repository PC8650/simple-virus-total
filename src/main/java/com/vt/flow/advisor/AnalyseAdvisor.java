package com.vt.flow.advisor;

import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.advisor.utils.ErrorRespUtil;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.remote.api.AnalyseApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.AnalyseResp;
import com.vt.remote.dto.vt.abs.UploadScanResp;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * 分析顾问，通过扫描响应的分析id，轮询分析状态直到超时或完成
 */
@Component
@RequiredArgsConstructor
public class AnalyseAdvisor implements CallAdvisor {

    private final AnalyseApi api;

    private final Thread.Builder virtualThreadBuilder = Thread.ofVirtual().name("Analyse-", 0);

    private final ConditionFactory conditionFactory = Awaitility.given()
            .pollThread(virtualThreadBuilder::start)
            .pollInterval(
                    new IterativePollInterval((duration) -> duration.multipliedBy(2), Duration.ofSeconds(3))
            )
            .atMost(Duration.ofSeconds(60));

    private VtResult<AnalyseResp> analyse(Scanner scanner, String analyseId) {
        try {
            return conditionFactory.until(
                    () -> scanner.apiRemote(() -> api.analyse(analyseId)),
                    (resp) -> !resp.isSuccess() || "completed".equals(resp.getData().attributes().status())
            );
        }catch (ConditionTimeoutException e) {
            VtResult<AnalyseResp> analyseResp = new VtResult<>();
            analyseResp.setError("Analyse Timeout");
            return analyseResp;
        }
    }

    @NotNull
    @Override
    public ChatClientResponse adviseCall(@NotNull ChatClientRequest chatClientRequest, @NotNull CallAdvisorChain callAdvisorChain) {
        Scanner scanner = ChainKey.SCANNER.get(chatClientRequest);
        UploadScanResp scanResp = ChainKey.SCAN_RESP.get(chatClientRequest);
        String analyseId = scanResp.getId();

        VtResult<AnalyseResp> analyseResp = analyse(scanner, analyseId);

        if (!analyseResp.isSuccess()) {
            return ErrorRespUtil.buildErrorResp(chatClientRequest, analyseResp.getError());
        }

        //判断最近分析时间是否超过一天
        Long date = analyseResp.getData().attributes().date();
        LocalDate lastDate = Instant.ofEpochMilli(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        if (lastDate.isBefore(LocalDate.now())) {
            //超过一天，重新分析
            VtResult<? extends UploadScanResp> reAnalyzeResp = scanner.reAnalyze(chatClientRequest);
            if (!reAnalyzeResp.isSuccess()) {
                return ErrorRespUtil.buildErrorResp(chatClientRequest, "ReAnalyze Error: " + reAnalyzeResp.getError());
            }
            //重新获取分析状态
            analyseResp = analyse(scanner, reAnalyzeResp.getData().getId());
            if (!analyseResp.isSuccess()) {
                return ErrorRespUtil.buildErrorResp(chatClientRequest, analyseResp.getError());
            }
        }

        return callAdvisorChain.nextCall(chatClientRequest);
    }

    @NotNull
    @Override
    public String getName() {
        return "AnalyseAdvisor";
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
