package com.vt.atp.dto.vt.domain.sub;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "流行排名")
public record PopularityRank(
        @Schema(description = "排名")
        Integer rank,
        @Schema(description = "UTC时间戳，排名被吸收时")
        long timestamp
) {
}
