package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "关于Microsoft .NET文件的信息")
public record DotNetAssembly(
        @SerializedName("assembly_data")
        @Schema(name = "assembly_data", description = "程序集清单的基本数据")
        AssemblyData assemblyData,
        @SerializedName("assembly_flags")
        @Schema(name = "assembly_flags", description = "关于程序集的其他标志（例如要求32位等）")
        Integer assemblyFlags,
        @SerializedName("assembly_flags_txt")
        @Schema(name = "assembly_flags_txt", description = "人类可读的 assembly_flags")
        String assemblyFlagsTxt,
        @SerializedName("assembly_name")
        @Schema(name = "assembly_name", description = "程序集名称")
        String assembly_name,
        @SerializedName("clr_meta_version")
        @Schema(name = "clr_meta_version", description = "通用语言运行时元数据的版本号")
        String clrMetaVersion,
        @SerializedName("clr_version")
        @Schema(name = "clr_version", description = "通用语言运行时版本")
        String clrVersion,
        @SerializedName("entry_point_rva")
        @Schema(name = "entry_point_rva", description = "入口点相对虚拟地址")
        String entryPointRva,
        @SerializedName("entry_point_token")
        @Schema(name = "entry_point_token", description = "项目入口点")
        String entryPointToken,
        @SerializedName("external_assemblies")
        @Schema(name = "external_assemblies", description = "使用的其他程序集，附有名称和版本。key 是名")
        Map<String, ExternalVersion> externalAssemblies,
        @SerializedName("exported_types")
        @Schema(name = "exported_types", description = "导出类型")
        List<ExportedType> exportedTypes,
        @SerializedName("external_files")
        @Schema(name = "external_files", description = "外部文件引用列表。vt官方文档未给出细节")
        List<Object> externalFiles,
        @SerializedName("external_modules")
        @Schema(name = "external_modules",description = "外部模块列表")
        List<String> externalModules,
        @SerializedName("manifest_resource")
        @Schema(name = "manifest_resource",description = "清单资源列表")
        List<String> manifestResource,
        @SerializedName("metadata_header_rva")
        @Schema(name = "metadata_header_rva", description = "元数据头相对虚拟地址")
        String metadata_header_rva,
        @SerializedName("resources_va")
        @Schema(name = "resources_va", description = "资源虚拟地址")
        String resourcesVa,
        @Schema(description = """
                关于程序集流、名称及相关数据的信息
                value是流名，值是一个包含以下字段的字典：
                    chi2: <float> chi-squared test value of stream data.
                    entropy: <float> entropy value of stream data.
                    md5: <string> md5 hash value of stream data.
                    size: <integer> size of stream.
                """)
        Map<String, String> streams,
        @SerializedName("strongname_va")
        @Schema(name = "strongname_va", description = "强名签名哈希的相对虚拟地址")
        String strongnameVa,
        @SerializedName("tables_present_map")
        @Schema(name = "tables_present_map", description = "当前表位图的十六进制值")
        String tablesPresentMap,
        @SerializedName("tables_present")
        @Schema(name = "tables_present", description = "程序集中存在的表格数量")
        Integer tablesPresent,
        @SerializedName("tables_rows_map")
        @Schema(name = "tables_rows_map", description = "每个表格行数的十六进制表示")
        String tablesRowsMap,
        @SerializedName("tables_rows_map_log")
        @Schema(name = "tables_rows_map_log", description = "当前表位图的十六进制值")
        String tablesRowsMapLog,
        @SerializedName("type_definition_list")
        @Schema(name = "type_definition_list", description = "每个条目代表一个类型定义")
        List<TypeDefinition> typeDefinitionList,
        @SerializedName("unmanaged_method_list")
        @Schema(name = "unmanaged_method_list", description = "外部模块方法列表")
        List<UnmanagedMethod> unmanagedMethodList
) {
    @Schema(description = "程序集基本数据")
    public record AssemblyData(
            @Schema(description = "构建号")
            Integer buildnumber,
            @Schema(description = "文化特定信息")
            String culture,
            @Schema(description = "组件的特定特性（如x86、AMD64等）")
            Integer flags,
            @SerializedName("flags_text")
            @Schema(name = "flags_text", description = "人类可读的flags")
            String flagsText,
            @Schema(description = "签名时使用的哈希ID")
            String hashalgid,
            @Schema(description = "主要版本")
            Integer majorversion,
            @Schema(description = "小版本")
            Integer minorversion,
            @Schema(description = "名称")
            String name,
            @Schema(description = "公钥")
            String pubkey,
            @Schema(description = "修订编号")
            Integer revisionnumber
    ){}
    @Schema(description = "external_assemblies 的 key")
    public record ExternalVersion(
            @Schema(description = "版本号")
            String version
    ){}
    @Schema(description = "导出类型")
    public record ExportedType(
            @Schema(description = "名称")
            String name,
            @Schema(description = "类型命名空间")
            String namespace
    ){}
    @Schema(description = "类型定义")
    public record TypeDefinition(
            @Schema(description = "命名空间")
            String namespace,
            @SerializedName("type_definitions")
            @Schema(name = "type_definitions", description = "类型定义")
            List<String> typeDefinitions
    ){}
    @Schema(description = "外部模块方法")
    public record UnmanagedMethod(
            @Schema(description = "方法名称")
            List<String> methods,
            @Schema(description = "模块名称")
            String name
    ){}
}
