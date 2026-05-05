package com.vt.atp.remote.api;

import com.google.gson.reflect.TypeToken;
import com.vt.atp.remote.api.constant.ApiConstant;
import com.vt.atp.remote.component.VtRemoter;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.AnalyseResp;
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
        String url = String.format(ApiConstant.PREFIX + ApiConstant.GET_ANALYSE, id);
        return vtRemoter.get(url, new TypeToken<VtResult<AnalyseResp>>() {});
    }

}
