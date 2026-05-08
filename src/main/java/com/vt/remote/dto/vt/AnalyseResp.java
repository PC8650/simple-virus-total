package com.vt.remote.dto.vt;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "分析结果响应")
public record AnalyseResp(
        @Schema(description = """
                分析id
                文件：文件id f-<sha256>-<timestamp>
                URL：u-sha256-timestamp(低位十六进制)
                """)
        String id,

        @Schema(description = "类型")
        String type,

        @Schema(description = "访问url")
        Link links,

        @Schema(description = "文件对象属性")
        Attributes attributes
) {

    public record Attributes(

            @Schema(description = "分析流程状态")
            String status,

            @Schema(description = "最近一次分析日期时间戳, 如果文件不是首传vt，会返回最近一次分析报告")
            Long date,

            @Schema(description = "统计数据")
            AnalyseStats stats,

            @Schema(description = "结果列表, 引擎名称-分析结果")
            Map<String, AnalyseResult> results
    ) {}

}
