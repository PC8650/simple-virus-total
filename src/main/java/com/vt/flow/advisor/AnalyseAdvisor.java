package com.vt.flow.advisor;

import com.vt.exception.WrapperException;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.utils.FlowSseUtil;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.flow.utils.PollUtil;
import com.vt.remote.api.AnalyseApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.AnalyseResp;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 分析顾问，通过扫描响应的分析id，轮询分析状态直到超时或完成
 */
@Component
@RequiredArgsConstructor
public class AnalyseAdvisor implements StreamAdvisor {

    private final AnalyseApi api;

    private VtResult<AnalyseResp> analyse(Scanner scanner, String analyseId, ChatClientRequest chatClientRequest) {
        AtomicReference<String> status = new AtomicReference<>("");
        return PollUtil.poll(300000L, 15000L,
                () -> {
                    FlowSseUtil.sendNotMainText(chatClientRequest, getName(),
                            "轮询分析状态 (15s 间隔, 上限5m)... ID: " + analyseId + " STATUS: " + status.get());
                    VtResult<AnalyseResp> analyseResp = scanner.apiRemote(() -> api.analyse(analyseId));
                    if (analyseResp.isSuccess()) status.set(analyseResp.getData().attributes().status());
                    return analyseResp;
                },
                (r) -> !r.isSuccess() || "completed".equals(r.getData().attributes().status()));
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Flux<ChatClientResponse> adviseStream(@NotNull ChatClientRequest chatClientRequest, @NotNull StreamAdvisorChain streamAdvisorChain) {
        ChainKey.CURRENT.get(chatClientRequest).set(getName());
        Scanner scanner = ChainKey.SCANNER.get(chatClientRequest);
        UploadScanResp scanResp = ChainKey.SCAN_RESP.get(chatClientRequest);
        String analyseId = scanResp.getId();

        VtResult<AnalyseResp> analyseResp = analyse(scanner, analyseId, chatClientRequest);

        if (!analyseResp.isSuccess())
            throw new WrapperException(analyseResp.getError());

        // 判断最近分析时间是否超过一天
        Long date = analyseResp.getData().attributes().date();
        LocalDate lastDate = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        if (lastDate.isBefore(LocalDate.now())) {
            // 超过一天，重新分析
            FlowSseUtil.sendNotMainText(chatClientRequest, getName(), "获取到陈旧报告记录，提交重新分析");
            VtResult<? extends UploadScanResp> reAnalyzeResp = scanner.reAnalyze(chatClientRequest);
            if (!reAnalyzeResp.isSuccess())
                throw new WrapperException(reAnalyzeResp.getError());
            // 重新获取分析状态
            analyseResp = analyse(scanner, reAnalyzeResp.getData().getId(), chatClientRequest);
            if (!analyseResp.isSuccess())
                throw new WrapperException(analyseResp.getError());
        }

        return streamAdvisorChain.nextStream(chatClientRequest);
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
