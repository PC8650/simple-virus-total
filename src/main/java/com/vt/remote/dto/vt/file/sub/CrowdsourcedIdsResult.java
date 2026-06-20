package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "文件的 IDS 匹配结果")
public record CrowdsourcedIdsResult(
        @SerializedName("alert_context")
        @Schema(name = "alert_context", description = "匹配警报上下文")
        List<IdsAlertContent> alertContext,
        @SerializedName("alert_severity")
        @Schema(name = "alert_severity", description = "警报严重程度：high/medium/low/info")
        String alertSeverity,
        @SerializedName("rule_category")
        @Schema(name = "rule_category", description = "警报类别")
        String ruleCategory,
        @SerializedName("rule_id")
        @Schema(name = "rule_id", description = "规则SID")
        String ruleId,
        @SerializedName("rule_msg")
        @Schema(name = "rule_msg", description = "警报描述")
        String ruleMsg,
        @SerializedName("rule_source")
        @Schema(name = "rule_source", description = "规则源，由SID范围确定")
        String ruleSource
) {
}
