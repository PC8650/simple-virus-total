package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "有关 Powershell 文件的信息")
public record PowershellInfo(
        @Schema(description = "脚本中使用的 cmdlet 列表")
        List<String> cmdlets,
        @SerializedName("cmdlets_alias")
        @Schema(name = "cmdlets_alias", description = "脚本中使用的 cmdlet 别名列表")
        List<String> cmdletsAlias,
        @SerializedName("dotnet_calls")
        @Schema(name = "dotnet_calls", description = "脚本中使用的 .Net 调用列表")
        List<String> dotnetCalls,
        @Schema(description = "脚本中定义的函数名称列表")
        List<String> functions,
        @SerializedName("ps_variables")
        @Schema(name = "ps_variables", description = "脚本使用的变量列表")
        List<String> psVariables
) {
}
