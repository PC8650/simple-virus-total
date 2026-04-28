package com.vt.atp.dto.vt;

import com.google.gson.annotations.SerializedName;

/**
 * 分析结果
 */
public record AnalyseResult(

        //分析方法
        String method,

        //引擎名称
        @SerializedName("engine_name")
        String engineName,

        //引擎版本
        @SerializedName("engine_version")
        String engineVersion,

        //引擎更新日期 yyyyMMdd
        @SerializedName("engine_update")
        String engineUpdate,

        //结果分类
        String category,

        //分析结果
        String result
) {
}
