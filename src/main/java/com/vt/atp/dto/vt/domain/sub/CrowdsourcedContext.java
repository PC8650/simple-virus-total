package com.vt.atp.dto.vt.domain.sub;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "众包背景")
public record CrowdsourcedContext(
        @Schema(description = "时间戳（UTC）")
        Long timestamp,
        @Schema(description = "严重程度")
        String severity,
        @Schema(description = "详细描述，会结合多种指标进行说明（例如域名排名、历史判定、已知良性/恶意特征）")
        String details,
        @Schema(description = "标题")
        String title,
        @Schema(description = "来源")
        String source
) {
}
