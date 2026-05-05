package com.vt.atp.flow.scan;

import com.vt.atp.exception.WrapperException;
import com.vt.atp.flow.dto.InputContent;
import com.vt.atp.flow.enums.TypeEnum;
import com.vt.atp.flow.scan.interfaces.Scanner;
import com.vt.atp.remote.api.FileApi;
import com.vt.atp.remote.dto.FileUpload;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.abs.UploadScanResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 文件扫描器
 */
@Component
@RequiredArgsConstructor
public class FileScanner implements Scanner {

    private final FileApi api;

    private final TypeEnum type = TypeEnum.FILE;

    @Override
    public VtResult<? extends UploadScanResp> scan(InputContent input) {
        if (input.getFile() == null) throw new WrapperException("must choose a file");
        FileUpload upload = new FileUpload();
        upload.setFile(input.getFile());
        upload.setPwd(input.getPwd());
        return api.uploadFile(upload.parse());
    }

    @Override
    public TypeEnum type() {
        return type;
    }

}
