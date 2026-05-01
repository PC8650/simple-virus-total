package com.vt.atp.api;

import com.google.gson.reflect.TypeToken;
import com.vt.atp.api.constant.ApiConstant;
import com.vt.atp.component.VtRemoter;
import com.vt.atp.dto.Result;
import com.vt.atp.dto.vt.AnalyseResp;
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
    public Result<AnalyseResp> analyse(String id) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.GET_ANALYSE, id);
        return vtRemoter.get(url, new TypeToken<Result<AnalyseResp>>() {});
    }

}
