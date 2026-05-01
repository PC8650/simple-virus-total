package com.vt.atp.api;

import com.google.api.client.http.EmptyContent;
import com.google.gson.reflect.TypeToken;
import com.vt.atp.api.constant.ApiConstant;
import com.vt.atp.component.VtRemoter;
import com.vt.atp.dto.Result;
import com.vt.atp.dto.vt.BaseScanResp;
import com.vt.atp.dto.vt.domain.DomainReportResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DomainApi {

    private final VtRemoter  vtRemoter;

    /**
     * 扫描 域名
     * @param domain 要扫描的域名
     * @return 分析id和链接
     */
    public Result<BaseScanResp> scanDomain(String domain) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.SCAN_DOMAIN, domain);
        return vtRemoter.post(url, new EmptyContent(), new TypeToken<Result<BaseScanResp>>(){});
    }

    /**
     * 获取域名报告
     * @param domain 域名
     * @return 域名报告
     */
    public Result<DomainReportResp> getDomainReport(String domain) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.GET_DOMAIN_REPORT, domain);
        return vtRemoter.get(url, new TypeToken<Result<DomainReportResp>>(){});
    }

}
