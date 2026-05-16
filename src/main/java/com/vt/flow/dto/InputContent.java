package com.vt.flow.dto;

import com.vt.flow.enums.TypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "输入内容")
public class InputContent {

    @Schema(description = "类型")
    private TypeEnum type;

    @Schema(description = "目标载荷")
    private String payload;

    @Schema(description = "文件")
    private MultipartFile file;

    @Schema(description = "压缩文件密码")
    private String pwd;

    @Schema(description = "补充说明")
    private String description;

    @Schema(description = "结果语言")
    private String language;


}
