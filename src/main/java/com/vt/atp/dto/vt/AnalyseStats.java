package com.vt.atp.dto.vt;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "分析结果")
public record AnalyseStats(

        @Schema(description = "判定为恶意的引擎数")
        Integer malicious,

        @Schema(description = "判定为可疑的引擎数")
        Integer suspicious,

        @Schema(description = "未被发现的引擎数")
        Integer undetected,

        @Schema(description = "判定为无害的引擎数")
        Integer harmless,

        @Schema(description = "超时的引擎数")
        Integer timeout,

        @SerializedName(value = "confirmed-timeout", alternate = {"confirmed_timeout"})
        @Schema(name = "confirmed-timeout", description = "确认超时的引擎数")
        Integer confirmedTimeout,

        @Schema(description = "失败的引擎数")
        Integer failure,

        @SerializedName(value = "type-unsupported", alternate = {"type_unsupported"})
        @Schema(name = "type-unsupported", description = "不支持文件类型的引擎数")
        Integer typeUnsupported
) {
}
