package com.vt.remote.dto.vt.url.sub;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "追踪器信息")
public record Tracker(
        @Schema(description = "追踪器ID（如有）")
        String id,
        @Schema(description = "追踪器采集日期作为UNIX时间戳")
        Long timestamp,
        @Schema(description = "追踪脚本 URL")
        String url
) {
}
