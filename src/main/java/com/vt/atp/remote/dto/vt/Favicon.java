package com.vt.atp.remote.dto.vt;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "favicon差分哈希和md5哈希。仅在高级API中返回")
public record Favicon(
        @SerializedName("raw_md5")
        @Schema(name = "raw_md5", description = "favicon's MD5 hash")
        String rawMd5,
        @Schema(description = "difference hash")
        String dhash
) {
}
