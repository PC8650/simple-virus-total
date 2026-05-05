package com.vt.atp.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

public record FileCondis(

        @SerializedName("raw_md5")
        @Schema(name = "raw_md5", description = "图标的 MD5 哈希值")
        String rawMd5,
        @Schema(description = "图标的差异哈希值。它可用于通过/intelligence/search端点搜索具有相似图标的文件")
        String dhash
) {}
