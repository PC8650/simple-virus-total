package com.vt.atp.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "有关Microsoft Windows PE 文件（即 exe、dll、驱动程序等）结构的信息：节、入口点、资源、导入、导出等")
public record PeInfo(
        @Schema(description = "如果存在，则包含调试信息")
        List<Debug> debug,
        @SerializedName("entry_point")
        @Schema(name = "entry_point", description = "可执行入口点")
        Integer entryPoint,
        @Schema(description = "导出的函数。它通常出现在 DLL 中，但不出现在 PE 中")
        List<String> exports,
        @Schema(description = "基于导入的哈希")
        String imphash,
        @SerializedName("import_list")
        @Schema(name = "import_list", description = "所有导入的函数")
        List<ImportFunc> importList,
        @SerializedName("machineType")
        @Schema(name = "machine_type", description = "此可执行文件的平台")
        Integer machineType,
        @Schema(description = "如果 PE 文件包含附加到末尾的信息，则表示有关该内容的一些信息")
        Overlay overlay,
        @SerializedName("resource_details")
        @Schema(name = "resource_details", description = "如果 PE 包含资源，则提供一些关于这些资源的信息")
        List<ResourceDetail> resourceDetails,
        @SerializedName("resource_langs")
        @Schema(name = "resource_langs", description = "资源中找到的语言摘要。键是语言（字符串），值是具有该语言的资源数量（整数）")
        Map<String, Integer> resourceLangs,
        @SerializedName("resource_types")
        @Schema(name = "resource_types", description = "资源类型摘要。键是资源类型（字符串），值是该特定类型的资源数量（整数）")
        Map<String, Integer> resourceTypes,
        @Schema(description = "PE片段信息列表")
        List<Section> sections,
        @Schema(description = "Unix Epoch 格式的编译时间")
        Long timestamp
) {
    @Schema(description = "调试信息")
    public record Debug(
            @Schema(description = "如果存在，则包含调试信息")
            CodeView codeview,
            @Schema(description = "当类型为 IMAGE_DEBUG_TYPE_FPO")
            Fpo fpo,
            @Schema(description = "当类型为 IMAGE_DEBUG_TYPE_MISC")
            Msic msic,
            @Schema(description = "此调试信息的位置")
            Integer offset,
            @Schema(description = "当类型为 IMAGE_DEBUG_TYPE_RESERVED10")
            Reserved10 reserved10,
            @Schema(description = "此调试信息块的大小")
            Integer size,
            @Schema(description = "日期。格式 %a %b %d %H:%M:%S %Y")
            String timestamp,
            @Schema(description = "调试类型信息")
            Integer type,
            @Schema(name = "type_str", description = "调试类型信息的人类可读版本")
            String typeStr
    ){
        @Schema(description = "代码调试信息")
        public record CodeView(
                @Schema(description = "始终递增的值")
                Integer age,
                @Schema(description = "唯一标识符。仅当签名是“RSDS”时返回")
                String guid,
                @Schema(description = "PDB 文件的路径")
                String name,
                @Schema(description = "设置为 0。仅当签名是“NB10”时返回")
                Integer offset,
                @Schema(description = "可以是“RSDS”或“NB10”")
                String signature,
                @Schema(description = "DBG 文件时间戳。签名中仅返回“NB10”")
                String timestamp
        ){}
        @Schema(description = "fpo信息")
        public record Fpo(
                @Schema(description = "包含 FP0 数据记录的数量")
                Integer functions
        ){}
        @Schema(description = "msic信息")
        public record Msic(
                @Schema(description = "始终设置为 1（IMAGE_DEBUG_MISC_EXENAME）")
                Integer dataType,
                @Schema(description = "记录的总长度，四舍五入到四字节的倍数")
                Integer length,
                @Schema(description = "如果数据是 Unicode 字符串，则为 1")
                Integer unicode,
                @Schema(description = "实际数据")
                String data,
                @Schema(description = "保留字节")
                String reserved
        ){}
        @Schema(description = "reserved10信息")
        public record Reserved10(
                @Schema(description = "它只包含 4 个字节，其值以十六进制格式存储")
                String value
        ){}
    }

    @Schema(description = "导入的函数")
    public record ImportFunc(
            @SerializedName("imported_functions")
            @Schema(name = "imported_functions", description = "导入的函数名称")
            List<String> importedFunctions,
            @SerializedName("library_name")
            @Schema(name = "library_name", description = "DLL 名称")
            String libraryName
    ){}

    @Schema(description = "PE 文件附加到末尾信息")
    public record Overlay(
            @Schema(description = "覆盖内容字节的卡方检验值")
            Double chi2,
            @Schema(description = "覆盖内容的字节熵值")
            Double entropy,
            @Schema(description = "如果能够识别出特定的文件格式，则会在此处提及")
            String filetype,
            @Schema(description = "覆盖内容的哈希值")
            String md5,
            @Schema(description = "覆盖层起始位置")
            Integer offset,
            @Schema(description = "以字节数表示")
            Integer size
    ){}

    @Schema(description = "PE 资源信息")
    public record ResourceDetail(
            @Schema(description = "资源内容的卡方检验")
            Double chi2,
            @Schema(description = "资源内容的熵值")
            Double entropy,
            @Schema(description = "表示能够识别特定文件格式")
            String filetype,
            @Schema(description = "资源的语言")
            String lang,
            @Schema(description = "资源内容的哈希值")
            String sha256,
            @Schema(description = "类型或资源")
            String type
    ){}

    @Schema(description = "PE片段信息")
    public record Section(
            @Schema(description = "覆盖内容字节的卡方检验值")
            Double chi2,
            @Schema(description = "覆盖内容的字节熵值")
            Double entropy,
            @Schema(description = "覆盖内容的哈希值")
            String md5,
            @Schema(description = "部分名称")
            String name,
            @SerializedName("raw_size")
            @Schema(name = "raw_size", description = "磁盘上已初始化数据的大小，以字节为单位")
            Integer rawSize,
            @SerializedName("virtual_address")
            @Schema(name = "virtual_address", description = "相对于映像基址，将节加载到内存中时第一个字节的地址")
            Integer virtualAddress,
            @SerializedName("virtual_size")
            @Schema(name = "virtual_size", description = "加载到内存中的部分总大小，以字节为单位")
            Integer virtualSize,
            @Schema(description = "内存访问权限缩写")
            String flags
    ){}
}
