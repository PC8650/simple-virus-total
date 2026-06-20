package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "VBA宏信息")
public record VbaInfo(
        @SerializedName("deobfuscated_strings")
        @Schema(name = "deobfuscated_strings", description = "包含找到的混淆字符串的连接")
        List<String> deobfuscatedStrings,
        @Schema(description = "长度大于 2 的字符串")
        List<String> strings
) {
}
