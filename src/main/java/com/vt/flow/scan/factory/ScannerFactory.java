package com.vt.flow.scan.factory;

import com.vt.enums.MsgEnum;
import com.vt.exception.WrapperException;
import com.vt.flow.dto.InputContent;
import com.vt.flow.enums.TypeEnum;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import com.vt.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫描器工厂
 */
@Component
public class ScannerFactory {

    private final Map<TypeEnum, Scanner> scannerMap = new HashMap<>();

    @Autowired
    public ScannerFactory(List<Scanner> scanners) {
        scanners.forEach(scanner -> scannerMap.put(scanner.type(), scanner));
    }

    public Scanner get(TypeEnum type) {
        Scanner scanner = scannerMap.get(type);
        if (scanner == null) {
            throw new WrapperException(MessageUtils.getMessage(MsgEnum.SYS_NO_SCANNER));
        }
        return scanner;
    }

    public VtResult<? extends UploadScanResp> scan(InputContent input) {
        return get(input.getType()).scan(input);
    }


}
