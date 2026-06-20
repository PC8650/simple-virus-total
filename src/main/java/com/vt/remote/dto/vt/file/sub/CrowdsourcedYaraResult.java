package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "根据众包规则匹配的 YARA")
public record CrowdsourcedYaraResult(
        @Schema(description = "规则作者")
        String author,
        @Schema(description = "规则描述")
        String description,
        @SerializedName("match_in_subfile")
        @Schema(name = "match_in_subfile", description = "是否在子文件中匹配")
        Boolean matchInSubfile,
        @SerializedName("rule_name")
        @Schema(name = "rule_name", description = "规则名称")
        String ruleName,
        @SerializedName("ruleset_id")
        @Schema(name = "ruleset_id", description = "VirusTotal 的规则集 ID。您可以使用此 ID 获取规则集信息 /api/v3/yara_rulesets/{id}")
        String rulesetId,
        @SerializedName("ruleset_name")
        @Schema(name = "ruleset_name", description = "匹配规则的规则集名称")
        String rulesetName,
        @Schema(description = "规则集来源")
        String source
) {
}
