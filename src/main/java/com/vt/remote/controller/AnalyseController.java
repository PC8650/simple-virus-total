package com.vt.remote.controller;

import com.vt.remote.api.AnalyseApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.AnalyseResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnalyseController {

    private final AnalyseApi analyseApi;


    @GetMapping("/get/analyse/{id}")
    public VtResult<AnalyseResp> getAnalyse(@PathVariable String id) {
        return analyseApi.analyse(id);
    }

}