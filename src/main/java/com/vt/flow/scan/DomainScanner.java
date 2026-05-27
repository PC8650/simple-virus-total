package com.vt.flow.scan;

import com.vt.enums.MsgEnum;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.dto.InputContent;
import com.vt.flow.enums.TypeEnum;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.remote.api.DomainApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import com.vt.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.DomainValidator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.stereotype.Component;

/**
 * 域名扫描器
 */
@Component
@RequiredArgsConstructor
public class DomainScanner implements Scanner {

    private final DomainApi api;

    private final TypeEnum type = TypeEnum.DOMAIN;

    private final DomainValidator validator = DomainValidator.getInstance();

    @Override
    public void valid(InputContent input) {
        if (!validator.isValid(input.getPayload())) {
            throw new IllegalArgumentException(MessageUtils.getMessage(MsgEnum.SCAN_ERR_DOMAIN_FORMAT));
        }
    }

    @Override
    public VtResult<? extends UploadScanResp> scan(InputContent input) {
        String payload = input.getPayload();
        return apiRemote(() -> api.scanDomain(payload), "Domain scan failed: ");
    }

    @Override
    public String getReportId(ChatClientRequest chatClientRequest) {
        InputContent inputContent = ChainKey.INPUT.get(chatClientRequest);
        return inputContent.getPayload();
    }

    @Override
    public VtResult<? extends UploadScanResp> reAnalyze(String target) {
        return apiRemote(() -> api.scanDomain(target), "Domain scan failed: ");
    }

    @Override
    public VtResult<?> getReport(String id) {
        return apiRemote(() -> api.getDomainReport(id), "Domain report fetching failed: ");
    }

    @Override
    public TypeEnum type() {
        return type;
    }
}
