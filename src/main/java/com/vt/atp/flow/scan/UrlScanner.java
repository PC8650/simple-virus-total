package com.vt.atp.flow.scan;

import com.vt.atp.exception.WrapperException;
import com.vt.atp.flow.dto.InputContent;
import com.vt.atp.flow.enums.TypeEnum;
import com.vt.atp.flow.scan.interfaces.Scanner;
import com.vt.atp.remote.api.UrlApi;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.abs.UploadScanResp;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
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
    public VtResult<? extends UploadScanResp> scan(InputContent input) {
        String payload = input.getPayload();
        if (!validator.isValid(payload)) throw new WrapperException("wrong url format");

        return api.scanUrl(payload);
    }

    @Override
    public TypeEnum type() {
        return type;
    }
}
