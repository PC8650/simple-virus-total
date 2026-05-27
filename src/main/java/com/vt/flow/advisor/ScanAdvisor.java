package com.vt.flow.advisor;

import com.vt.enums.MsgEnum;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.component.CacheManager;
import com.vt.flow.dto.CacheDto;
import com.vt.flow.dto.InputContent;
import com.vt.flow.scan.factory.ScannerFactory;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.flow.utils.FlowSseUtil;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import com.vt.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ScanAdvisor implements StreamAdvisor {

    private final ScannerFactory scannerFactory;
    private final CacheManager cacheManager;

    private boolean cached(InputContent inputContent, ChatClientRequest chatClientRequest) {
        String cacheKey = inputContent.getType().getCacheKey(inputContent);
        CacheDto cache = cacheManager.get(cacheKey);

        boolean cached = Objects.nonNull(cache);
        if (!cached) cache = CacheDto.init(cacheKey);
        ChainKey.CACHE.put(chatClientRequest, cache);

        return cached;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Flux<ChatClientResponse> adviseStream(@NotNull ChatClientRequest chatClientRequest, @NotNull StreamAdvisorChain streamAdvisorChain) {
        ChainKey.CURRENT.get(chatClientRequest).set(getName());
        InputContent inputContent = ChainKey.INPUT.get(chatClientRequest);

        String lang = inputContent.getLanguage();
        FlowSseUtil.sendNotMainText(chatClientRequest, getName(),
                MessageUtils.getMessage(lang, MsgEnum.SSE_SCAN_SUBMIT, inputContent.getType()));

        Scanner scanner = scannerFactory.get(inputContent.getType());
        scanner.valid(inputContent);
        if (!cached(inputContent, chatClientRequest)) {
            // 不存在缓存，才提交扫描
            VtResult<? extends UploadScanResp> scanResult = scanner.scan(inputContent);
            ChainKey.SCAN_RESP.put(chatClientRequest, scanResult.getData());
        }
        ChainKey.SCANNER.put(chatClientRequest, scanner);

        return streamAdvisorChain.nextStream(chatClientRequest);
    }

    @NotNull
    @Override
    public String getName() {
        return "ScanAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
