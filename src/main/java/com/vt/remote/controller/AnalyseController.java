package com.vt.remote.controller;

import com.vt.remote.api.AnalyseApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.AnalyseResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "通用分析管理", description = "提供对 VirusTotal 分析任务状态的查询能力")
@RestController
@RequiredArgsConstructor
public class AnalyseController {

    private final AnalyseApi analyseApi;


    @Operation(summary = "查询分析状态", description = "通过分析 ID (analyseId) 获取任务的实时进度与元数据")
    @GetMapping("/get/analyse/{id}")
    public VtResult<AnalyseResp> getAnalyse(@Parameter(description = "分析任务唯一 ID") @PathVariable String id) {
        return analyseApi.analyse(id);
    }

}