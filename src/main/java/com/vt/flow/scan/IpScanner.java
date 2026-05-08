package com.vt.flow.scan;

import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.dto.InputContent;
import com.vt.flow.enums.TypeEnum;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.remote.api.IpApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.stereotype.Component;

/**
 * ip 扫描器
 */
@Component
@RequiredArgsConstructor
public class IpScanner implements Scanner {

    private final IpApi api;

    private final TypeEnum type = TypeEnum.IP;

    private final InetAddressValidator validator = InetAddressValidator.getInstance();

    @Override
    public VtResult<? extends UploadScanResp> scan(InputContent input) {
        String payload = input.getPayload();
        if (!validator.isValid(payload)) {
            VtResult<? extends UploadScanResp> result = new VtResult<>();
            result.setError("Wrong ip format");
            return result;
        }

        return apiRemote(() -> api.scanIp(payload));
    }

    @Override
    public String getReportId(ChatClientRequest chatClientRequest) {
        InputContent inputContent = ChainKey.INPUT.get(chatClientRequest);
        return inputContent.getPayload();
    }

    @Override
    public VtResult<? extends UploadScanResp> reAnalyze(String target) {
        return apiRemote(() -> api.scanIp(target));
    }

    @Override
    public VtResult<?> getReport(String id) {
        return apiRemote(() -> api.getIpReport(id));
    }

    @Override
    public TypeEnum type() {
        return type;
    }
}
