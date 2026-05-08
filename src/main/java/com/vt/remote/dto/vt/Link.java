package com.vt.remote.dto.vt;

/**
 * 分析和报告返回的请求地址
 * @param self 如果分析响应结果，为分析结果获取地址。否则为报告结果获取地址
 * @param item 如果分析响应结果，为报告结果获取地址
 */
public record Link(
        String self,
        String item
) {

}
