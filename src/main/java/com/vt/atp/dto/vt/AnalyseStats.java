package com.vt.atp.dto.vt;

import com.google.gson.annotations.SerializedName;

/**
 * 分析结果
 */
public record AnalyseStats(

        //判定为恶意的引擎数
        Integer malicious,

        //判定为可疑的引擎数
        Integer suspicious,

        //未被发现的引擎数
        Integer undetected,

        //判定为无害的引擎数
        Integer harmless,

        //超时的引擎数
        Integer timeout,

        //确认超时的引擎数
        @SerializedName("confirmed-timeout")
        Integer confirmedTimeout,

        //失败的引擎数
        Integer failure,

        //不支持文件类型的引擎数
        @SerializedName("type-unsupported")
        Integer typeUnsupported
) {
}
