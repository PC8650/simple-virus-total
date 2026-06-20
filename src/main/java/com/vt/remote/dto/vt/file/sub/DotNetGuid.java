package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Microsoft .NET 程序集的标识符")
public record DotNetGuid(
        @Schema(description = "ModuleVersionID 在生成时产生，每次生成都会生成一个新的 GUID")
        String mvid,
        @SerializedName("typelib_id")
        @Schema(name = "typelib_id", description = "TypeLib ID（如果存在），在创建新项目时由 Visual Studio 默认生成")
        String typelibId
) {
}
