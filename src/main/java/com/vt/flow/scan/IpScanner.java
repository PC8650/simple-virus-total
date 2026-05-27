package com.vt.flow.scan;

import com.vt.enums.MsgEnum;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.dto.InputContent;
import com.vt.flow.enums.TypeEnum;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.remote.api.IpApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import com.vt.utils.MessageUtils;
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
    public void valid(InputContent input) {
        if (!validator.isValid(input.getPayload())) {
            throw new IllegalArgumentException(MessageUtils.getMessage(MsgEnum.SCAN_ERR_IP_FORMAT));
        }
    }

    @Override
    public VtResult<? extends UploadScanResp> scan(InputContent input) {
        String payload = input.getPayload();
        return apiRemote(() -> api.scanIp(payload), "Ip scan failed: ");
    }

    @Override
    public String getReportId(ChatClientRequest chatClientRequest) {
        InputContent inputContent = ChainKey.INPUT.get(chatClientRequest);
        return inputContent.getPayload();
    }

    @Override
    public VtResult<? extends UploadScanResp> reAnalyze(String target) {
        return apiRemote(() -> api.scanIp(target), "Ip scan failed: ");
    }

    @Override
    public VtResult<?> getReport(String id) {
        return apiRemote(() -> api.getIpReport(id), "Ip report fetching failed: ");
    }

    @Override
    public TypeEnum type() {
        return type;
    }
}
