package com.vt.atp.controller;

import com.vt.atp.api.AnalyseApi;
import com.vt.atp.dto.Result;
import com.vt.atp.dto.vt.AnalyseResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnalyseController {

    private final AnalyseApi analyseApi;


    @GetMapping("/get/analyse/{id}")
    public Result<AnalyseResp> getAnalyse(@PathVariable String id) {
        return analyseApi.analyse(id);
    }

}