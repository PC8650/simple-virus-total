package com.vt.atp.flow.scan;

import com.vt.atp.exception.WrapperException;
import com.vt.atp.flow.dto.InputContent;
import com.vt.atp.flow.enums.TypeEnum;
import com.vt.atp.flow.scan.interfaces.Scanner;
import com.vt.atp.remote.api.IpApi;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.abs.UploadScanResp;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.InetAddressValidator;
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
        if (!validator.isValid(payload)) throw new WrapperException("wrong ip format");

        return api.scanIp(payload);
    }

    @Override
    public TypeEnum type() {
        return type;
    }
}
