package com.vt.atp.flow.scan;

import com.vt.atp.exception.WrapperException;
import com.vt.atp.flow.dto.InputContent;
import com.vt.atp.flow.enums.TypeEnum;
import com.vt.atp.flow.scan.interfaces.Scanner;
import com.vt.atp.remote.api.DomainApi;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.abs.UploadScanResp;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.DomainValidator;
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
    public VtResult<? extends UploadScanResp> scan(InputContent input) {
        String payload = input.getPayload();
        if (!validator.isValid(payload)) throw new WrapperException("wrong domain format");

        return api.scanDomain(payload);
    }

    @Override
    public TypeEnum type() {
        return type;
    }
}
