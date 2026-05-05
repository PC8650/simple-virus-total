package com.vt.atp.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "联系域名/IP证书")
public record Tls(
        @Schema(description = "证书发行者信息。密钥是证书字段（C、CN、O 等），字符串和值始终是字符串")
        Map<String, String> issuer,
        @Schema(description = "证书JA3")
        String ja3,
        @Schema(description = "certificate JA3s")
        String ja3s,
        @SerializedName("serial_number")
        @Schema(name = "serial_number", description = "证书序列号")
        String serialNumber,
        @Schema(description = "证书的服务器名称指示")
        String sni,
        @Schema(description = "证书主题信息。密钥是证书字段（C、CN、O 等），字符串和值始终是字符串")
        Map<String, String> subject,
        @Schema(description = "证书指纹")
        String thumbprint,
        @Schema(description = "TLS版本")
        String version
) {
}
