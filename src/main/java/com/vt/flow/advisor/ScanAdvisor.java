package com.vt.flow.advisor;

import com.vt.exception.WrapperException;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.dto.InputContent;
import com.vt.flow.scan.factory.ScannerFactory;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.stereotype.Component;
import com.vt.flow.utils.FlowSseUtil;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class ScanAdvisor implements StreamAdvisor {

    private final ScannerFactory scannerFactory;

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Flux<ChatClientResponse> adviseStream(@NotNull ChatClientRequest chatClientRequest, @NotNull StreamAdvisorChain streamAdvisorChain) {
        ChainKey.CURRENT.get(chatClientRequest).set(getName());
        InputContent inputContent = ChainKey.INPUT.get(chatClientRequest);

        FlowSseUtil.sendNotMainText(chatClientRequest, getName(), "正在提交扫描任务... 类型：" + inputContent.getType());

        Scanner scanner = scannerFactory.get(inputContent.getType());
        VtResult<? extends UploadScanResp> scanResult = scanner.scan(inputContent);

        if (!scanResult.isSuccess()) {
            throw new WrapperException(scanResult.getError());
        }

        ChainKey.SCAN_RESP.put(chatClientRequest, scanResult.getData());
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
