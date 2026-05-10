package com.vt.flow.dto;


import com.vt.flow.enums.TypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "报告内容")
public class ReportContent {

    @Schema(description = "类型")
    private TypeEnum type;

    @Schema(description = "gui访问链接")
    private String url;

    @Schema(description = "报告")
    private Object report;

    @Schema(description = "行为报告，文件分析时才有")
    private Object behaviour;

    @Schema(description = "行为报告中的ATT&CK汇总信息")
    private Object mitre;

    @Schema(description = "错误信息")
    private String error;
}
