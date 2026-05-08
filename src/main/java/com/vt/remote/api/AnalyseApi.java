package com.vt.remote.api;

import com.google.gson.reflect.TypeToken;
import com.vt.remote.api.enums.ApiEnum;
import com.vt.remote.component.VtRemoter;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.AnalyseResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyseApi {

    private final VtRemoter vtRemoter;

    /**
     * 获取分析结果
     * @param id 分析id
     * @return 分析结果
     */
    public VtResult<AnalyseResp> analyse(String id) {
        String url = ApiEnum.GET_ANALYSE.getApiUrl(id);
        return vtRemoter.get(url, new TypeToken<VtResult<AnalyseResp>>() {});
    }

}
