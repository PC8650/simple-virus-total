package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "IDS警报")
public record IdsAlert(
        @SerializedName("alert_context")
        @Schema(name = "alert_context", description = "匹配警报上下文")
        AlertContext alertContext,
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
){

    @Schema(description = "警报上下文")
    public record AlertContext(
            @SerializedName("dest_ip")
            @Schema(name = "dest_ip", description = "目的IP")
            String destIp,
            @SerializedName("dest_port")
            @Schema(name = "dest_port", description = "目的端口")
            String destPort,
            @Schema(description = "目的主机名")
            String hostname,
            @Schema(description = "通信协议名称")
            String protocol,
            @SerializedName("src_ip")
            @Schema(name = "src_ip", description = "源IP")
            String srcIp,
            @SerializedName("src_port")
            @Schema(name = "src_port", description = "源端口")
            String srcPort,
            @Schema(description = "如果警报与 HTTP 通信相关，则使用目的 URL")
            String url
    ){}
}
