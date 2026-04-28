package com.vt.atp.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import com.vt.atp.dto.vt.Ref;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "匹配签名信息")
public record SignatureMatches(
        @Schema(description = "标识")
        String id,
        @Schema(description = "以下任一格式：SIG_FORMAT_UNKNOWN、SIG_FORMAT_YARA、SIG_FORMAT_SIGMA、SIG_FORMAT_CAPA 或 SIG_FORMAT_OPENIOC")
        String format,
        @Schema(description = "作者名单")
        List<String> authors,
        @SerializedName("rule_src")
        @Schema(name = "rule_src", description = "规则来源")
        String ruleSrc,
        @Schema(description = "规则名称")
        String name,
        @Schema(description = "规则描述")
        String description,
        @SerializedName("match_data")
        @Schema(name = "match_data", description = "匹配数据")
        List<String> matchData,
        @Schema(description = "严重性")
        String severity,
        @Schema(description = "引用信息")
        List<Ref> refs
) {}
