package com.vt.remote.dto.vt.ip;

import com.google.gson.annotations.SerializedName;
import com.vt.remote.dto.vt.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "ip报告响应")
public record IpReportResp(

        @Schema(description = "id，ip")
        String id,

        @Schema(description = "报告对象类型")
        String type,

        @Schema(description = "结果链接")
        Link links,

        @Schema(description = "IP 对象属性")
        Attributes attributes

) {

    @Schema(description = "IP 对象属性")
    public record Attributes(

            @SerializedName("as_owner")
            @Schema(name = "as_owner", description = "IP 所属自治系统的所有者")
            String asOwner,

            @Schema(description = "IP 所属的自治系统编号")
            Integer asn,

            @Schema(description = "IP 地址所在的洲（ISO-3166 洲代码）")
            String continent,

            @Schema(name = "IP 地址所在的国家/地区（ISO-3166 国家/地区代码）")
            String country,

            @Schema(description = "IP 地址 JARM 哈希")
            String jarm,

            @SerializedName("last_analysis_date")
            @Schema(name = "last_analysis_date", description = "UTC时间戳，表示该IP最后一次被扫描的时间")
            Long lastAnalysisDate,

            @SerializedName("last_analysis_results")
            @Schema(name = "last_analysis_results", description = "最新扫描结果。引擎名称-结果")
            Map<String, AnalyseResult> lastAnalysisResults,

            @SerializedName("last_analysis_stats")
            @Schema(name = "last_analysis_stats", description = "最新扫描结果摘要")
            AnalyseStats lastAnalysisStats,

            @SerializedName("last_https_certificate")
            @Schema(name = "last_https_certificate", description = "上次分析该IP时检索的SSL证书对象")
            SslCertificate lastHttpsCertificate,

            @SerializedName("last_https_certificate_date")
            @Schema(name = "last_https_certificate_date", description = "VirusTotal检索证书的日期（UTC时间戳）")
            Long lastHttpsCertificateDate,

            @SerializedName("last_modification_date")
            @Schema(name = "last_modification_date", description = "UTC时间戳，代表最后修改日期")
            Long lastModificationDate,

            @Schema(description = "IP 地址所属的 IP 网络范围")
            String network,

            @SerializedName("regional_internet_registry")
            @Schema(name = "regional_internet_registry", description = "RIR（AFRINIC、ARIN、APNIC、LACNIC 或 RIPE NCC）")
            String regionalInternetRegistry,

            @Schema(description = "VT社区投票数值")
            Integer reputation,

            @Schema(name = "标识属性")
            List<String> tags,

            @SerializedName("total_votes")
            @Schema(name = "total_votes", description = "社区投票总数的未加权值，分为“无害”和“恶意”两类")
            Vote totalVotes,

            @Schema(description = " whois 信息，来自相关 whois 服务器")
            String whois,

            @SerializedName("whois_date")
            @Schema(name = "whois_date", description = "VirusTotal whois 记录最后更新日期")
            Long whoisDate
    ){}

}
