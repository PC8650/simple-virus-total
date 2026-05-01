package com.vt.atp.api;

import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.UrlEncodedContent;
import com.google.gson.reflect.TypeToken;
import com.vt.atp.api.constant.ApiConstant;
import com.vt.atp.component.VtRemoter;
import com.vt.atp.dto.Result;
import com.vt.atp.dto.vt.BaseScanResp;
import com.vt.atp.dto.vt.url.UrlReportResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UrlApi {

    private final VtRemoter vtRemoter;

    /**
     * 扫描 url
     * @param targetUrl 要扫描的url
     * @return 分析id和链接
     */
    public Result<BaseScanResp> scanUrl(String targetUrl) {
        String url = ApiConstant.PREFIX + ApiConstant.SCAN_URL;
        return vtRemoter.post(url, new UrlEncodedContent(Map.of("url", targetUrl)), new TypeToken<Result<BaseScanResp>>(){});
    }

    /**
     * 重新分析 url
     * @param id sha256
     * @return 分析id和链接
     */
    public Result<BaseScanResp> reAnalyze(String id) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.RE_ANALYSE_URL, id);
        return vtRemoter.post(url, new EmptyContent(), new TypeToken<Result<BaseScanResp>>(){});
    }

    /**
     * 获取url报告
     * @param id sha256
     * @return url报告
     */
    public Result<UrlReportResp> getUrlReport(String id) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.GET_URL_REPORT, id);
        return vtRemoter.get(url, new TypeToken<Result<UrlReportResp>>(){});
    }

}
