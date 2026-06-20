package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "关于Debian软件包的信息")
public record DebInfo(
        @Schema(description = "关于项目打包版本变更的信息")
        Changelog changelog,
        @SerializedName("control_metadata")
        @Schema(description = "包元数据信息。字段可能会因包而异，所有值都是字符串。https://www.debian.org/doc/debian-policy/ch-controlfields.html#list-of-fields")
        Map<String, String> controlMetadata,
        @SerializedName("control_scripts")
        @Schema(name = "control_scripts", description = "用于包管理操作的脚本")
        ControlScripts controlScripts,
        @SerializedName("structural_metadata")
        @Schema(name = "structural_metadata", description = "包结构信息")
        StructuralMetadata structuralMetadata
) {
    @Schema(description = "关于项目打包版本变更的信息")
    public record Changelog(
            @SerializedName("Author")
            @Schema(name = "Author", description = "作者名")
            String author,
            @SerializedName("Date")
            @Schema(name = "Date", description = "构建最后编辑日期 格式 %a, %d %b %Y %H:%M%S %z")
            String date,
            @SerializedName("Debian revision")
            @Schema(name = "Debian revision", description = "系统修订")
            String debianRevision,
            @SerializedName("Debian version")
            @Schema(name = "Debian version", description = "系统版本")
            String debianVersion,
            @SerializedName("Distributions")
            @Schema(name = "Distributions", description = "包含该版本软件包应安装的分发版的（空格分隔）名称")
            String distributions,
            @SerializedName("Full version")
            @Schema(name = "Full version", description = "完整系统版本")
            String fullVersion,
            @SerializedName("Package")
            @Schema(name = "Package", description = "包类型")
            String packages,
            @SerializedName("Urgency")
            @Schema(name = "Urgency", description = "描述从之前版本升级到此版本的重要性。可能的数值包括 low/medium/high/emergency/critical")
            String urgency,
            @SerializedName("Version history")
            @Schema(name = "Version history", description = "系统版本历史")
            String versionHistory
    ){}

    @Schema(description = "用于包管理操作的脚本")
    public record ControlScripts(
            @Schema(description = "安装后运行的脚本")
            String postinst,
            @Schema(description = "移除后运行的脚本")
            String postrm,
            @Schema(description = "安装前运行的脚本")
            String preinst,
            @Schema(description = "移除前运行的脚本")
            String prerm
    ){}

    @Schema(description = "包结构信息")
    public record StructuralMetadata(
            @SerializedName("contained_files")
            @Schema(name = "contained_files", description = "包内的文件数量")
            Integer containedFiles,
            @SerializedName("contained_items")
            @Schema(name = "contained_items", description = "包内的文件和目录数量")
            Integer containedItems,
            @SerializedName("max_date")
            @Schema(name = "max_date", description = "最近的子文件修改日期。%Y-%m-%d %H:%M%S")
            String maxDate,
            @SerializedName("min_date")
            @Schema(name = "min_date", description = "最早的子文件修改日期。%Y-%m-%d %H:%M%S")
            String minDate
    ){}
}
