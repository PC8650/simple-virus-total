package com.vt.remote.dto.vt.url;

import com.google.gson.annotations.SerializedName;
import com.vt.remote.dto.vt.*;
import com.vt.remote.dto.vt.url.sub.Tracker;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "url报告响应")
public record UrlReportResp(
        @Schema(description = "id，<sha256>")
        String id,

        @Schema(description = "报告对象类型")
        String type,

        @Schema(description = "结果链接")
        Link links,

        @Schema(description = "URL 对象属性")
        Attributes attributes
) {

    @Schema(description = "URL 对象属性")
    public record Attributes(

            @Schema(description = "类别。键是分类该 URL 的合作伙伴，值是根据该合作伙伴的分类")
            Map<String, String> categories,

            @Schema(description = "包括URL同档的差分哈希和md5哈希。仅在高级API中返回")
            Favicon favicon,

            @SerializedName("first_submission_date")
            @Schema(name = "first_submission_date", description = "url 在vt首次提交的UTC时间戳")
            Long firstSubmissionDate,

            @SerializedName("html_meta")
            @Schema(name = "html_meta", description = "包含所有元标签（仅适用于下载HTML的URL）。密钥是元标签名称，值是包含该元标签所有值的列表")
            Map<String, List<String>> htmlMeta,

            @SerializedName("last_analysis_date")
            @Schema(name = "last_analysis_date", description = "UTC时间戳，表示该URL最后一次被扫描的时间")
            Long lastAnalysisDate,

            @SerializedName("last_analysis_results")
            @Schema(name = "last_analysis_results", description = "最新扫描结果。引擎名称-结果")
            Map<String, AnalyseResult> lastAnalysisResults,

            @SerializedName("last_analysis_stats")
            @Schema(name = "last_analysis_stats", description = "最新扫描结果摘要")
            AnalyseStats lastAnalysisStats,

            @SerializedName("last_final_url")
            @Schema(name = "last_final_url", description = "如果原始URL重定向，它在哪里结束")
            String lastFinalUrl,

            @SerializedName("last_http_response_code")
            @Schema(name = "last_http_response_code", description = "上次响应的HTTP响应代码")
            Integer lastHttpResponseCode,

            @SerializedName("last_http_response_content_length")
            @Schema(name = "last_http_response_content_length", description = "接收内容的字节长度")
            Long lastHttpResponseContentLength,

            @SerializedName("last_http_response_content_sha256")
            @Schema(name = "last_http_response_content_sha256", description = "URL响应正文的SHA256哈希")
            String lastHttpResponseContentSha256,

            @SerializedName("last_http_response_cookies")
            @Schema(name = "last_http_response_cookies", description = "包含网站的cookies")
            Map<String, String> lastHttpResponseCookies,

            @SerializedName("last_http_response_headers")
            @Schema(name = "last_http_response_headers", description = "包含上次HTTP响应的头部和值")
            Map<String, String> lastHttpResponseHeaders,

            @SerializedName("last_modification_date")
            @Schema(name = "last_modification_date", description = "UTC时间戳，代表最后修改日期")
            Long lastModificationDate,

            @SerializedName("last_submission_date")
            @Schema(name = "last_submission_date", description = "UTC时间戳，代表上次送去分析的时间")
            Long lastSubmissionDate,

            @SerializedName("outgoing_links")
            @Schema(name = "outgoing_links", description = "包含指向不同域名的链接")
            List<String> outgoingLinks,

            @SerializedName("redirection_chain")
            @Schema(name = "redirection_chain", description = "访问给定URL时所跟踪的重定向历史。链的最后一个URL不包含在列表中，它可以在属性last_final_url中获得")
            List<String> redirectionChain,

            @Schema(description = "VT社区投票数值")
            Integer reputation,

            @Schema(description = "标签")
            List<String> tags,

            @Schema(description = "域名后缀")
            String tld,

            @SerializedName("targeted_brand")
            @Schema(name = "targeted_brand", description = "从钓鱼引擎中提取的定向品牌信息")
            Map<String, String> targetedBrand,

            @SerializedName("times_submitted")
            @Schema(name = "times_submitted", description = "该URL被检查的次数")
            Integer timesSubmitted,

            @Schema(description = "网页标题")
            String title,

            @SerializedName("total_votes")
            @Schema(name = "total_votes", description = "社区投票总数的未加权值，分为“无害”和“恶意”两类")
            Vote totalVotes,

            @Schema(description = "以历史方式包含该URL中所有发现的追踪器。每个密钥都是一个追踪器名称")
            Map<String, List<Tracker>> trackers,

            @Schema(description = "扫描的原始URL")
            String url,

            @SerializedName("has_content")
            @Schema(name = "has_content", description = "URL是否包含内容")
            Boolean hasContent
    ){}

}
