package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "从反病毒软件判定和聚类哈希中提取的人类可读名称")
public record PopularThreatClassification(
        @SerializedName("popular_threat_category")
        @Schema(name = "popular_threat_category", description = "恶意软件类别令牌列表，按频率降序排列。这些令牌经过标准化处理，例如 'ransom' 变为 'ransomware'")
        List<ThreatToken> popularThreatCategory,
        @SerializedName("popular_threat_name")
        @Schema(name = "popular_threat_name", description = "恶意软件家族令牌列表，按频率降序排列。'value' 是令牌，'count' 是提及该令牌的反病毒引擎数量")
        List<ThreatToken> popularThreatName,
        @SerializedName("suggested_threat_label")
        @Schema(name = "suggested_threat_label", description = "结合了部分热门威胁类别和热门威胁名称的建议标签字符串")
        String suggestedThreatLabel
) {
    @Schema(description = "威胁令牌详情")
    public record ThreatToken(
            @Schema(description = "提及该令牌的反病毒引擎数量")
            Integer count,
            @Schema(description = "令牌值")
            String value
    ) {}
}
