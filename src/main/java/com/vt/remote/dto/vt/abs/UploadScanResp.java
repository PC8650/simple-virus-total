package com.vt.remote.dto.vt.abs;


import com.vt.remote.dto.vt.Link;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "上传/扫描响应 抽象类")
public abstract class UploadScanResp {

    @Schema(description = "类型")
    private String type;

    @Schema(description = """
            分析id
            文件：base64(sha256:timestamp)
            URL：u-sha256-timestamp(低位十六进制)
            IP：i-sha256-timestamp(低位十六进制)
            DOMAIN：d-sha256-timestamp(低位十六进制)
            """)
    private String id;

    @Schema(description = "结果链接")
    private Link links;

    public abstract String getIdSha256();

}
