package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Sigma分析沙盒生成的EVTX文件结果")
public record SigmaAnalysisResult(
        @SerializedName("rule_title")
        @Schema(name = "rule_title", description = "匹配的Sigma规则称号")
        String ruleTitle,
        @SerializedName("rule_source")
        @Schema(name = "rule_source", description = "σ规则集，该规则属于哪里")
        String ruleSource,
        @SerializedName("match_context")
        @Schema(name = "match_context", description = "具体匹配事件")
        MatchContext matchContext,
        @SerializedName("rule_level")
        @Schema(name = "rule_level", description = "规则级别，critical, high, medium, low")
        String ruleLevel,
        @SerializedName("rule_description")
        @Schema(name = "rule_description", description = "规则描述")
        String ruleDescription,
        @SerializedName("rule_author")
        @Schema(name = "rule_author", description = "规则作者")
        String ruleAuthor,
        @SerializedName("rule_id")
        @Schema(name = "rule_id", description = "VirusTotal 中的规则 ID。可以用它找到符合相同规则的其他文件")
        String ruleId
) {

    @Schema(description = "匹配事件")
    public record MatchContext(
            @Schema(description = "所有匹配事件以键值表示")
            Map<String, String> values
    ){}
}
