package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "技术")
public record Technique(
        @Schema(description = "技术编号")
        String id,
        @Schema(description = "技术名称")
        String name,
        @Schema(description = "指向 MITRE 官方网站该技术的详细说明链接")
        String link,
        @Schema(description = "技术的官方详细描述。解释该技术的原理、常见操作系统 API 以及攻击者如何利用它")
        String description,
        @Schema(description = "特征/签名数组。这是最底层、最核心的证据，说明沙箱是根据什么具体行为触发了这条 MITRE 规则")
        List<Signature> signatures
) {

    @Schema(description = "签名/证据")
    public record Signature(
            @Schema(description = "严重程度。常见值包括 INFO（信息）、LOW（低）、MEDIUM（中）、HIGH（高）、UNKNOWN（未知）。注：很多基础 API 调用会被标记为 INFO，需要结合上下文判断")
            String severity,
            @Schema(description = "触发该签名的具体行为描述")
            String description,
            @SerializedName("match_data")
            @Schema(name = "match_data", description = "匹配到的原始数据/参数")
            List<String> matchData
    ){}
}
