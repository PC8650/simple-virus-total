package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "从 PCAP 网络捕获中提取的流量概念")
public record TrafficInspection(
        @Schema(description = "HTTP 请求列表")
        List<HttpInspection> http
) {
    @Schema(description = "HTTP 流量检查详情")
    public record HttpInspection(
            @SerializedName("binary_hash")
            @Schema(name = "binary_hash", description = "下载内容的 SHA256")
            String binaryHash,
            @SerializedName("binary_magic")
            @Schema(name = "binary_magic", description = "下载内容的文件类型")
            String binaryMagic,
            @Schema(description = "下载日期，格式为 %Y-%m-%d %H:%M:%S.%f")
            String datetime,
            @SerializedName("interesting_magic")
            @Schema(name = "interesting_magic", description = "有趣的魔数")
            Integer interestingMagic,
            @Schema(description = "HTTP 请求方法")
            String method,
            @SerializedName("remote_host")
            @Schema(name = "remote_host", description = "请求目的地，包括 IP 和端口")
            String remoteHost,
            @SerializedName("response_code")
            @Schema(name = "response_code", description = "HTTP 响应代码")
            String responseCode,
            @SerializedName("response_size")
            @Schema(name = "response_size", description = "响应大小，以字节为单位")
            String responseSize,
            @Schema(description = "请求 URL")
            String url,
            @SerializedName("user-agent")
            @Schema(name = "user-agent", description = "客户端用户代理")
            String userAgent
    ) {}
}
