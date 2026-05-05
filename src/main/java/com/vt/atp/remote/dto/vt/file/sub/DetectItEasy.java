package com.vt.atp.remote.dto.vt.file.sub;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Detect It Easy（简称“DIE”）是一个用于确定文件类型的程序。该程序定义了MSDOS、PE、ELF、MACH和二进制文件类型")
public record DetectItEasy(
        @Schema(description = "文件类型：PE32, PE64, ELF32, ELF64, Mach-O64")
        String filetype,
        @Schema(description = "文件中检测到的工件列表")
        List<Value> values
){
    @Schema(description = "文件中检测到的工件")
    public record Value(
            @Schema(description = "工件的上下文（如 Native、GUI32、NRV等）")
            String info,
            @Schema(description = "工件版本")
            String version,
            @Schema(description = "检测的一般类型（如 Linker、Compiler、Packer等）")
            String type,
            @Schema(description = "项目特定名称（如 UPX、Microsoft Linker、gcc(GNU)等）")
            String name
    ){}
}
