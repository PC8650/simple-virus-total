package com.vt.remote.dto.vt.domain;

import com.google.gson.annotations.SerializedName;
import com.vt.remote.dto.vt.*;
import com.vt.remote.dto.vt.domain.sub.CrowdsourcedContext;
import com.vt.remote.dto.vt.domain.sub.DnsRecord;
import com.vt.remote.dto.vt.domain.sub.PopularityRank;
import com.vt.remote.dto.vt.SslCertificate;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "域名报告响应")
public record DomainReportResp(

        @Schema(description = "id，域名")
        String id,

        @Schema(description = "报告对象类型")
        String type,

        @Schema(description = "结果链接")
        Link links,

        @Schema(description = "域名 对象属性")
        Attributes attributes

) {

    @Schema(description = "域名 对象属性")
    public record Attributes(

            @Schema(description = "顶级域名")
            String tld,

            @Schema(description = "将分类服务与其分配的领域类别联系起来。这些服务包括：Alexa、BitDefender、TrendMicro、Websense ThreatSeeker 等")
            Map<String, String> categories,

            @SerializedName("creation_date")
            @Schema(name = "creation_date", description = "创建日期，取自域名的whois（UTC时间戳）")
            Long creationDate,

            @Schema(description = "包含域 Favicon 的差分哈希和 MD5 哈希的词典。仅限高级用户使用")
            Favicon favicon,

            @Schema(description = "域名的 JARM 哈希")
            String jarm,

            @SerializedName("last_analysis_date")
            @Schema(name = "last_analysis_date", description = "最近一次扫描日期。UTC 时间戳")
            Long lastAnalysisDate,

            @SerializedName("last_analysis_results")
            @Schema(name = "last_analysis_results", description = "最新扫描结果。引擎名称-结果")
            Map<String, AnalyseResult> lastAnalysisResults,

            @SerializedName("last_analysis_stats")
            @Schema(name = "last_analysis_stats", description = "最新扫描结果摘要")
            AnalyseStats lastAnalysisStats,

            @SerializedName("last_dns_records")
            @Schema(name = "last_dns_records", description = "域名最后一次扫描记录中的dns记录")
            List<DnsRecord> last_dns_records,

            @SerializedName("last_dns_records_date")
            @Schema(name = "last_dns_records_date", description = "由VirusTotal检索DNS记录列表的日期（UTC时间戳）")
            Long lastDnsRecordsDate,

            @SerializedName("last_https_certificate")
            @Schema(name = "last_https_certificate", description = "上次分析该域名时检索的SSL证书对象")
            SslCertificate lastHttpsCertificate,

            @SerializedName("last_https_certificate_date")
            @Schema(name = "last_https_certificate_date", description = "VirusTotal检索证书的日期（UTC时间戳）")
            Long lastHttpsCertificateDate,

            @SerializedName("last_modification_date")
            @Schema(name = "last_modification_date", description = "域名信息最后更新日期")
            Long lastModificationDate,

            @SerializedName("last_update_date")
            @Schema(name = "last_update_date", description = "更新日期取自whois（UTC时间戳）")
            Long lastUpdateDate,

            @SerializedName("popularity_ranks")
            @Schema(name = "popularity_ranks", description = "域名在Alexa、Quantcast、Statvoo等流行度排名中的位置。rank name - rank")
            Map<String, PopularityRank> popularityRanks,

            @Schema(description = "注册域名的公司")
            String registrar,

            @Schema(description = "域名得分，由VirusTotal社区投票计算得出")
            Integer reputation,

            @Schema(description = "代表属性列表")
            List<String> tags,

            @SerializedName("total_votes")
            @Schema(name = "total_votes", description = "社区投票总数的未加权值，分为“无害”和“恶意”两类")
            Vote totalVotes,

            @Schema(description = " whois 信息，来自相关 whois 服务器")
            String whois,

            @SerializedName("whois_date")
            @Schema(name = "whois_date", description = "VirusTotal whois 记录最后更新日期")
            Long whoisDate,

            @SerializedName("expiration_date")
            @Schema(name = "expiration_date", description = "域名过期时间戳")
            Long expirationDate,

            @SerializedName("crowdsourced_context")
            @Schema(name = "crowdsourced_context", description = "众包安全上下文信息")
            List<CrowdsourcedContext> crowdsourcedContext
    ){}

}
