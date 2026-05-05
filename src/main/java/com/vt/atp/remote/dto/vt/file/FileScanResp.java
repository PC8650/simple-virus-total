package com.vt.atp.remote.dto.vt.file;

import com.vt.atp.remote.dto.vt.abs.UploadScanResp;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.StringUtils;

import java.util.Base64;

@Schema(description = "文件上传扫描响应")
public class FileScanResp extends UploadScanResp {

    @Override
    public String getIdSha256() {
        String id = super.getId();
        if (!StringUtils.hasText(id)) return "";

        String decoded = new String(Base64.getDecoder().decode(id));
        return decoded.substring(0, decoded.indexOf(":"));
    }

}
