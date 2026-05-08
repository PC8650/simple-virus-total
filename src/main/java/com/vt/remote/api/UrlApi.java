package com.vt.remote.api;

import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.UrlEncodedContent;
import com.google.gson.reflect.TypeToken;
import com.vt.remote.api.enums.ApiEnum;
import com.vt.remote.component.VtRemoter;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.BaseScanResp;
import com.vt.remote.dto.vt.url.UrlReportResp;
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
    public VtResult<BaseScanResp> scanUrl(String targetUrl) {
        String url = ApiEnum.SCAN_URL.getApiUrl(null);
        return vtRemoter.post(url, new UrlEncodedContent(Map.of("url", targetUrl)), new TypeToken<VtResult<BaseScanResp>>(){});
    }

    /**
     * 重新分析 url
     * @param id sha256
     * @return 分析id和链接
     */
    public VtResult<BaseScanResp> reAnalyze(String id) {
        String url = ApiEnum.RE_ANALYSE_URL.getApiUrl(id);
        return vtRemoter.post(url, new EmptyContent(), new TypeToken<VtResult<BaseScanResp>>(){});
    }

    /**
     * 获取url报告
     * @param id sha256
     * @return url报告
     */
    public VtResult<UrlReportResp> getUrlReport(String id) {
        String url = ApiEnum.GET_URL_REPORT.getApiUrl(id);
        return vtRemoter.get(url, new TypeToken<VtResult<UrlReportResp>>(){});
    }

}
