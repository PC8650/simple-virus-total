package com.vt.flow.advisor;

import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.advisor.utils.ErrorRespUtil;
import com.vt.flow.dto.InputContent;
import com.vt.flow.scan.factory.ScannerFactory;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.stereotype.Component;

/**
 * 扫描顾问，用于处理上传分析用户目标
 */
@Component
@RequiredArgsConstructor
public class ScanAdvisor implements CallAdvisor {

    private final ScannerFactory scannerFactory;

    @NotNull
    @Override
    public ChatClientResponse adviseCall(@NotNull ChatClientRequest chatClientRequest, @NotNull CallAdvisorChain callAdvisorChain) {
        InputContent inputContent = ChainKey.INPUT.get(chatClientRequest);
        Scanner scanner = scannerFactory.get(inputContent.getType());
        VtResult<? extends UploadScanResp> scanResult = scanner.scan(inputContent);

        if (!scanResult.isSuccess()) {
            return ErrorRespUtil.buildErrorResp(chatClientRequest, scanResult.getError());
        }

        ChainKey.SCAN_RESP.put(chatClientRequest, scanResult.getData());
        ChainKey.SCANNER.put(chatClientRequest, scanner);

        return callAdvisorChain.nextCall(chatClientRequest);
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
