package com.vt.remote.dto.vt.file.sub;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "IDS结果统计")
public record CrowdsourcedIdsStats(
        Integer high,
        Integer medium,
        Integer critical,
        Integer low
) {
}
