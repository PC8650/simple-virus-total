package com.vt.atp.dto.vt.file;

import com.vt.atp.dto.vt.Link;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Base64;

@Data
public class FileUploadResp {

    @Schema(description = "类型")
    private String type;

    @Schema(description = "id，base64(sha256:timestamp)")
    private String id;

    @Schema(description = "结果链接")
    private Link links;


    public String parseIdToSha256() {
        if (!StringUtils.hasText(id)) return "";

        String decoded = new String(Base64.getDecoder().decode(id));
        return decoded.substring(0, decoded.indexOf(":"));
    }

}
