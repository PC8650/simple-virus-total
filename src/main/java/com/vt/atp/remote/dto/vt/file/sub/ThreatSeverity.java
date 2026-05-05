package com.vt.atp.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "威胁验证程度")
public record ThreatSeverity(
        @SerializedName("last_analysis_date")
        @Schema(name = "last_analysis_date", description = "时间戳，表示计算威胁严重性的时间")
        Long lastAnalysisDate,
        @SerializedName("threat_severity_level")
        @Schema(name = "threat_severity_level", description = """
                威胁严重程度
                SEVERITY_NONE这是分配给被判定为非恶意实体的级别。
                SEVERITY_LOW该威胁可能影响较小，但仍应密切监测。
                SEVERITY_MEDIUM：表示存在需要注意的潜在威胁。
                SEVERITY_HIGH建议立即采取行动；该威胁可能造成严重影响。
                SEVERITY_UNKNOWN：数据不足，无法评估严重程度。
                """)
        String threatSeverityLevel,
        @SerializedName("level_description")
        @Schema(name = "level_description", description = "用于描述确定严重程度的信号的人类可读描述")
        String levelDescription,
        @Schema(description = "版本")
        Integer version,
        @SerializedName("threat_severity_data")
        @Schema(name = "threat_severity_data", description = "威胁验证程度数据")
        ThreatSeverityData threatSeverityData

) {

    @Schema(description = "威胁验证程度数据")
    public record ThreatSeverityData(
            @SerializedName("popular_threat_category")
            @Schema(name = "popular_threat_category", description = "热门威胁类别")
            String popularThreatCategory,
            @SerializedName("type_tag")
            @Schema(name = "type_tag", description = "类型标签")
            String typeTag,
            @SerializedName("has_similar_files_with_detections")
            @Schema(name = "has_similar_files_with_detections", description = "vhash 检测到与此类似的文件")
            Boolean hasSimilarFilesWithDetections,
            @SerializedName("is_matched_by_crowdsourced_yara_with_detections")
            @Schema(name = "is_matched_by_crowdsourced_yara_with_detections", description = "至少有 1 条 yara 规则与此文件匹配，并检测到其他文件")
            Boolean isMatchedByCrowdsourcedYaraWithDetections,
            @SerializedName("has_vulnerabilities")
            @Schema(name = "has_vulnerabilities", description = "该文件受 CVE 漏洞影响")
            Boolean hasVulnerabilities,
            @SerializedName("can_be_detonated")
            @Schema(name = "can_be_detonated", description = " 该文件已在沙箱中进行了特性分析（行为）")
            Boolean canBeDetonated,
            @SerializedName("has_legit_tag")
            @Schema(name = "has_legit_tag", description = "文件具有“合法”标签")
            Boolean hasLegitTag,
            @SerializedName("num_gav_detections")
            @Schema(name = "num_gav_detections", description = "Google 防病毒软件检测到的病毒数量")
            Integer numGavDetections,
            @SerializedName("has_execution_parents_with_detections")
            @Schema(name = "has_execution_parents_with_detections", description = " 父文件已检测")
            Boolean hasExecutionParentsWithDetections,
            @SerializedName("has_dropped_files_with_detections")
            @Schema(name = "has_dropped_files_with_detections", description = "已丢弃的文件有检测结果")
            Boolean hasDroppedFilesWithDetections,
            @SerializedName("has_contacted_ips_with_detections")
            @Schema(name = "has_contacted_ips_with_detections", description = "已联系过检测到的 IP 地址、域名和 URL")
            Boolean hasContactedIpsWithDetections,
            @SerializedName("has_contacted_urls_with_detections")
            @Schema(name = "has_contacted_urls_with_detections", description = "已联系检测到的url")
            Boolean hasContactedUrlsWithDetections,
            @SerializedName("has_contacted_domains_with_detections")
            @Schema(name = "has_contacted_domains_with_detections", description = "已联系检测到的域")
            Boolean hasContactedDomainsWithDetections,
            @SerializedName("has_embedded_ips_with_detections")
            @Schema(name = "has_embedded_ips_with_detections", description = "是否嵌入了带有检测功能的 IP 地址")
            Boolean hasEmbeddedIpsWithDetections,
            @SerializedName("has_embedded_domains_with_detections")
            @Schema(name = "has_embedded_domains_with_detections", description = "是否包含带有检测结果的嵌入式域")
            Boolean hasEmbeddedDomainsWithDetections,
            @SerializedName("has_embedded_urls_with_detections")
            @Schema(name = "has_embedded_urls_with_detections", description = "是否包含带有检测结果的嵌入式 URL")
            Boolean hasEmbeddedUrlsWithDetections,
            @SerializedName("has_malware_configs")
            @Schema(name = "has_malware_configs", description = "有恶意软件配置")
            Boolean hasMalwareConfigs,
            @SerializedName("has_references")
            @Schema(name = "has_references", description = "有参考文献")
            Boolean hasReferences,
            @SerializedName("belongs_to_threat_actor")
            @Schema(name = "belongs_to_threat_actor", description = "属于威胁行为者")
            Boolean belongsToThreatActor,
            @SerializedName("belongs_to_bad_collection")
            @Schema(name = "belongs_to_bad_collection", description = "属于不良合集")
            Boolean belongsToBadCollection,
            @SerializedName("num_av_detections")
            @Schema(name = "num_av_detections", description = "常规 AV 检测次数（如有）")
            Integer numAvDetections,
            @SerializedName("has_bad_sandbox_verdicts")
            @Schema(name = "has_bad_sandbox_verdicts", description = "动态分析已将该文件识别为恶意文件")
            Boolean hasBadSandboxVerdicts
    ){}
}
