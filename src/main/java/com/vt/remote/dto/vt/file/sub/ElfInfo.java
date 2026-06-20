package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "关于Unix ELF文件的信息")
public record ElfInfo(
        @SerializedName("export_list")
        @Schema(name = "export_list", description = "导出元素")
        List<Item> exportList,
        @Schema(description = "文件的一些描述性元数据")
        Header header,
        @SerializedName("import_list")
        @Schema(name = "import_list", description = "导入元素")
        List<Item> importList,
        @Schema(description = "包含可执行文件的打包器（如果有的话）")
        List<String> packers,
        @SerializedName("section_list")
        @Schema(name = "section_list", description = "节")
        List<Section> sectionList,
        @SerializedName("segment_list")
        @Schema(name = "segment_list", description = "片段")
        List<segment> segmentList,
        @SerializedName("shared_libraries")
        @Schema(name = "shared_libraries", description = "包含该可执行文件使用的共享库")
        List<String> sharedLibraries
) {
    @Schema(description = "项")
    public record Item(
            @Schema(description = "项名称")
            String name,
            @Schema(description = "项类型")
            String type
    ){}
    @Schema(description = "关于该文件的一些描述性元数据")
    public record Header(
            @Schema(description = "文件类型")
            String type,
            @SerializedName("hdr_version")
            @Schema(name = "hdr_version", description = "头部版本")
            String hdrVersion,
            @SerializedName("num_prog_headers")
            @Schema(name = "num_prog_headers", description = "程序头部的条目数量")
            Integer numProgHeaders,
            @SerializedName("os_abi")
            @Schema(name = "os_abi", description = "应用二进制接口类型")
            String osAbi,
            @SerializedName("obj_version")
            @Schema(name = "obj_version", description = "0x1原始ELF文件")
            String objVersion,
            @Schema(description = "平台")
            String machine,
            @Schema(description = "可执行入口点")
            String entrypoint,
            @SerializedName("num_section_headers")
            @Schema(name = "num_section_headers", description = "节头部数量")
            String numSectionHeaders,
            @SerializedName("abi_version")
            @Schema(name = "abi_version", description = "应用二进制接口版本")
            Integer abiVersion,
            @Schema(description = "内存中的数据对齐")
            String data,
            @SerializedName("class")
            @Schema(name = "class", description = "文件类型")
            String clasz
    ){}
    @Schema(description = "节")
    public record Section(
            @Schema(description = "名称")
            String name,
            @SerializedName("virtual_address")
            @Schema(name = "virtual_address", description = "虚拟地址")
            String virtualAddress,
            @Schema(description = "节标志")
            String flags,
            @SerializedName("physical_offset")
            @Schema(name = "physical_offset", description = "物理偏移")
            String physicalOffset,
            @SerializedName("section_type")
            @Schema(name = "section_type", description = "节类型")
            String sectionType,
            @Schema(description = "以字节为单位的节大小")
            String size
    ){}
    @Schema(description = "段")
    public record segment(
            @SerializedName("segment_type")
            @Schema(name = "segment_type", description = "类型")
            String segmentType,
            @Schema(description = "涉及的资源列表")
            List<String> resources
    ){}
}
