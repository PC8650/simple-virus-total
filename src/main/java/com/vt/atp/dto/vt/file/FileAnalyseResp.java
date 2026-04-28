package com.vt.atp.dto.vt.file;

import com.vt.atp.dto.vt.AnalyseResult;
import com.vt.atp.dto.vt.AnalyseStats;
import com.vt.atp.dto.vt.Link;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "文件分析响应")
public record FileAnalyseResp(

        @Schema(description = "文件id f-<sha256>-<timestamp>")
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
