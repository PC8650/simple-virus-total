package com.vt.remote.dto;

import com.vt.remote.api.constant.SizeConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Schema(description = "文件上传参数")
public class FileUpload {

    @Schema(description = "待上传的二进制文件")
    private MultipartFile file;

    @Schema(description = "压缩包解压密码（选填）")
    private String pwd;

    public FileUploadParse parse() {
        Integer fileSize = Integer.valueOf(new BigDecimal(file.getSize()).divide(SizeConstant.B2M_DIVIDE, 0, RoundingMode.UP).toPlainString());
        return new FileUploadParse(file, fileSize, file.getOriginalFilename(), pwd);
    }

}
