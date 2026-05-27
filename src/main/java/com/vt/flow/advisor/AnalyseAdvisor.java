package com.vt.flow.advisor;

import com.vt.exception.WrapperException;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.component.CacheManager;
import com.vt.flow.dto.CacheDto;
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
import com.vt.enums.MsgEnum;
import com.vt.utils.MessageUtils;

/**
 * 分析顾问，通过扫描响应的分析id，轮询分析状态直到超时或完成
 */
@Component
@RequiredArgsConstructor
public class AnalyseAdvisor implements StreamAdvisor {

    private final AnalyseApi api;
    private final CacheManager cacheManager;

    private VtResult<AnalyseResp> analyse(Scanner scanner, String analyseId, ChatClientRequest chatClientRequest) {
        com.vt.flow.dto.InputContent inputContent = ChainKey.INPUT.get(chatClientRequest);
        String lang = inputContent.getLanguage();

        FlowSseUtil.sendNotMainText(chatClientRequest, getName(),
                MessageUtils.getMessage(lang, MsgEnum.SSE_ANALYSE_START, analyseId));
        return PollUtil.poll(300000L, 15000L,
                () -> {
                    VtResult<AnalyseResp> analyseResp = scanner.apiRemote(() -> api.analyse(analyseId),
                            "Analyse status fetch failed: ");
                    String status = analyseResp.getData().attributes().status();
                    FlowSseUtil.sendNotMainText(chatClientRequest, getName(),
                            MessageUtils.getMessage(lang, MsgEnum.SSE_ANALYSE_POLLING, status));
                    return analyseResp;
                },
                (r) -> "completed".equals(r.getData().attributes().status()));
    }

    private void analyseStatusFetch(String analyseId, CacheDto cache, ChatClientRequest chatClientRequest) {
        Scanner scanner = ChainKey.SCANNER.get(chatClientRequest);
        analyse(scanner, analyseId, chatClientRequest);

        // 分析完成，为缓存设置报告id
        cache.setReportId(cache.getKey());
        cacheManager.put(cache);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Flux<ChatClientResponse> adviseStream(@NotNull ChatClientRequest chatClientRequest, @NotNull StreamAdvisorChain streamAdvisorChain) {
        ChainKey.CURRENT.get(chatClientRequest).set(getName());

        String analyseId;
        CacheDto cache = ChainKey.CACHE.get(chatClientRequest);
        UploadScanResp scanResp = ChainKey.SCAN_RESP.get(chatClientRequest);

        if (scanResp != null) {
            // scanResp 不为null，上一步提交了扫描请求
            analyseId = scanResp.getId();
            cache.setAnalyseId(analyseId);
            cacheManager.put(cache);
            analyseStatusFetch(analyseId, cache, chatClientRequest);
        } else if (cache.hasReportId()) {
            // 已有缓存的报告id
            String lang = ChainKey.INPUT.get(chatClientRequest).getLanguage();
            FlowSseUtil.sendNotMainText(chatClientRequest, getName(),
                    MessageUtils.getMessage(lang, MsgEnum.SSE_ANALYSE_CACHED));
        } else if (cache.hasAnalyseId()) {
            // 只有分析id
            analyseStatusFetch(cache.getAnalyseId(), cache, chatClientRequest);
        } else {
            throw new WrapperException(MessageUtils.getMessage(MsgEnum.SYS_ANALYSE_ID_ERROR));
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
