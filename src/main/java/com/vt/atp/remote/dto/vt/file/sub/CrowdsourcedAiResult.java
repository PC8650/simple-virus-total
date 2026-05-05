package com.vt.atp.remote.dto.vt.file.sub;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "众包AI结果")
public record CrowdsourcedAiResult(
        @Schema(description = "代码片段的自然语言摘要")
        String analysis,
        @Schema(description = "结果来源")
        String source,
        @Schema(description = "crowdsourced_ai 结果的 id")
        String id,
        @Schema(description = "判定")
        String verdict,
        @Schema(description = "类别")
        String category
) {
}
