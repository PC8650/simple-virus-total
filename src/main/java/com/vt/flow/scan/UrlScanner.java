package com.vt.flow.scan;

import com.vt.enums.MsgEnum;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.dto.InputContent;
import com.vt.flow.enums.TypeEnum;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.remote.api.UrlApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import com.vt.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.stereotype.Component;

/**
 * url 扫描器
 */
@Component
@RequiredArgsConstructor
public class UrlScanner implements Scanner {

    private final UrlApi api;

    private final TypeEnum type = TypeEnum.URL;

    private final UrlValidator validator = new UrlValidator();

    @Override
    public void valid(InputContent input) {
        if (!validator.isValid(input.getPayload())) {
            throw new IllegalArgumentException(MessageUtils.getMessage(MsgEnum.SCAN_ERR_URL_FORMAT));
        }
        String url = input.getPayload();
        if (!url.endsWith("/")) input.setPayload(url + "/");
    }

    @Override
    public VtResult<? extends UploadScanResp> scan(InputContent input) {
        String payload = input.getPayload();
        return apiRemote(() -> api.scanUrl(payload),  "Url scan failed: ");
    }

    @Override
    public String getReportId(ChatClientRequest chatClientRequest) {
        UploadScanResp scanResp = ChainKey.SCAN_RESP.get(chatClientRequest);
        return scanResp.getIdSha256();
    }

    @Override
    public VtResult<? extends UploadScanResp> reAnalyze(String target) {
        return apiRemote(() -> api.reAnalyze(target), "Url scan failed: ");
    }

    @Override
    public VtResult<?> getReport(String id) {
        return apiRemote(() -> api.getUrlReport(id), "Url report fetching failed: ");
    }

    @Override
    public TypeEnum type() {
        return type;
    }
}
