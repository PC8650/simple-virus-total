package com.vt.remote.dto.vt.file;

import com.vt.remote.dto.vt.file.sub.Technique;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "文件行为 战术/技术 汇总响应")
public record FileMitreResp(
        @Schema(description = "战术列表")
        List<Tactic> tactics
) {

    @Schema(description = "战术")
    public record Tactic(
            @Schema(description = "战术编号")
            String id,
            @Schema(description = "战术名称")
            String name,
            @Schema(description = "指向 MITRE 官方网站该战术的详细说明链接")
            String link,
            @Schema(description = "战术的官方详细描述。解释攻击者采用该战术的总体意图")
            String description,
            @Schema(description = "技术数组。为了达成该战术目的，攻击者具体使用了哪些技术手段")
            List<Technique> techniques
    ){}

}
