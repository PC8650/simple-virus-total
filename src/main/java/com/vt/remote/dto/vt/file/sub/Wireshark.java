package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Wireshark 工具在处理文件时生成的元数据，仅适用于 PCAP 网络捕获")
public record Wireshark(
        @Schema(description = "包含 DNS 请求及其解析结果的列表。每个子列表包含两个项目：要解析的域名（String）和解析后的 IP 地址列表（List of Strings）")
        List<List<Object>> dns,
        @Schema(description = "PCAP 捕获元数据")
        Pcap pcap
) {
    @Schema(description = "PCAP 捕获元数据详情")
    public record Pcap(
            @SerializedName("Capture duration")
            @Schema(name = "Capture duration", description = "持续时间，以秒为单位")
            String captureDuration,
            @SerializedName("Data size")
            @Schema(name = "Data size", description = "PCAP 文件的人类可读大小")
            String dataSize,
            @SerializedName("End time")
            @Schema(name = "End time", description = "停止捕获的日期，格式为 %Y-%m-%d %H:%M:%S")
            String endTime,
            @SerializedName("File encapsulation")
            @Schema(name = "File encapsulation", description = "文件封装类型")
            String fileEncapsulation,
            @SerializedName("File type")
            @Schema(name = "File type", description = "文件类型，通常为 pcap")
            String fileType,
            @SerializedName("Number of packets")
            @Schema(name = "Number of packets", description = "网络捕获中数据包的人类可读数量")
            String numberOfPackets,
            @SerializedName("Start time")
            @Schema(name = "Start time", description = "开始捕获的日期，格式为 %Y-%m-%d %H:%M:%S")
            String startTime
    ) {
    }
}
