package com.vt.atp.remote.dto.vt;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "分析结果")
public record AnalyseResult(

        @Schema(description = "分析方法")
        String method,

        @SerializedName("engine_name")
        @Schema(name = "engine_name", description = "引擎名称")
        String engineName,

        @SerializedName("engine_version")
        @Schema(name = "engine_version", description = "引擎版本")
        String engineVersion,

        @SerializedName("engine_update")
        @Schema(name = "engine_update", description = "引擎更新日期 yyyyMMdd")
        String engineUpdate,

        @Schema(description = "结果分类")
        String category,

        @Schema(description = "分析结果")
        String result
) {
}
