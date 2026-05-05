package com.vt.atp.remote.dto;

import com.vt.atp.remote.api.constant.SizeConstant;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class FileUpload {

    private MultipartFile file;

    private String pwd;

    public FileUploadParse parse() {
        Integer fileSize = Integer.valueOf(new BigDecimal(file.getSize()).divide(SizeConstant.B2M_DIVIDE, 0, RoundingMode.UP).toPlainString());
        return new FileUploadParse(file, fileSize, file.getOriginalFilename(), pwd);
    }

}
