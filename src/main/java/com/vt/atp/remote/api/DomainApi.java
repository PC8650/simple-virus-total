package com.vt.atp.remote.api;

import com.google.api.client.http.EmptyContent;
import com.google.gson.reflect.TypeToken;
import com.vt.atp.remote.api.constant.ApiConstant;
import com.vt.atp.remote.component.VtRemoter;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.BaseScanResp;
import com.vt.atp.remote.dto.vt.domain.DomainReportResp;
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
    public VtResult<BaseScanResp> scanDomain(String domain) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.SCAN_DOMAIN, domain);
        return vtRemoter.post(url, new EmptyContent(), new TypeToken<VtResult<BaseScanResp>>(){});
    }

    /**
     * 获取域名报告
     * @param domain 域名
     * @return 域名报告
     */
    public VtResult<DomainReportResp> getDomainReport(String domain) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.GET_DOMAIN_REPORT, domain);
        return vtRemoter.get(url, new TypeToken<VtResult<DomainReportResp>>(){});
    }

}
