package com.vt.remote.dto.vt.file.sub;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "PCAP 网络捕获的匹配 Suricata 警报详细信息")
public record Suricata(
        @Schema(description = "有关警报检测内容的简要摘要")
        String alert,
        @Schema(description = "流量分类（例如 Potentially Bad Traffic）")
        String classification,
        @Schema(description = "网络捕获中与规则匹配的字符串列表，字符串以 %Y-%m-%d %H:%M:%S.%f 格式的日期开头")
        List<String> destinations
) {
}
