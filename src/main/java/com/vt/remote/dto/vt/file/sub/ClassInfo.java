package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "关于 Java .class 字节码文件的信息")
public record ClassInfo(
        @Schema(description = "在该类中使用了的常量")
        List<String> constants,
        @SerializedName("extends")
        @Schema(name = "extends", description = "继承的类")
        String extend,
        @SerializedName("implements")
        @Schema(name = "implements", description = "实现的接口")
        List<String> implement,
        @Schema(description = "该类的方法")
        List<String> methods,
        @Schema(description = "类名称")
        String name,
        @Schema(description = "源自主版本号和小版本号")
        String platform,
        @Schema(description = "提供的类、字段和方法")
        List<String> provides,
        @Schema(description = "要求的类、字段和方法")
        List<String> requires
) {
}
