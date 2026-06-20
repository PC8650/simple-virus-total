package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ids警报上下文")
public record IdsAlertContent(
        @SerializedName("dest_ip")
        @Schema(name = "dest_ip", description = "目的IP")
        String destIp,
        @SerializedName("dest_port")
        @Schema(name = "dest_port", description = "目的端口")
        String destPort,
        @Schema(description = "目的主机名")
        String hostname,
        @Schema(description = "通信协议名称")
        String protocol,
        @SerializedName("src_ip")
        @Schema(name = "src_ip", description = "源IP")
        String srcIp,
        @SerializedName("src_port")
        @Schema(name = "src_port", description = "源端口")
        String srcPort,
        @Schema(description = "如果警报与 HTTP 通信相关，则使用目的 URL")
        String url
) {
}
