package com.vt.atp.flow.scan.factory;

import com.vt.atp.exception.WrapperException;
import com.vt.atp.flow.dto.InputContent;
import com.vt.atp.flow.enums.TypeEnum;
import com.vt.atp.flow.scan.interfaces.Scanner;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.abs.UploadScanResp;
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
        if (scanner == null) throw new WrapperException("no scanner of this type exists");
        return scanner;
    }

    public VtResult<? extends UploadScanResp> scan(InputContent input) {
        return get(input.getType()).scan(input);
    }


}
