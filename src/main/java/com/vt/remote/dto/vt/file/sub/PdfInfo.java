package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "有关 Adobe PDF 文件的信息")
public record PdfInfo(
        @Schema(description = "文档中发现的 /AcroForm 标签数量，AcroForm 是交互式表单")
        Integer acroform,
        @Schema(description = "文档中发现的 /AA 标签数量，AutoAction 定义了响应影响整个文档的各种触发事件而采取的操作")
        Integer autoaction,
        @SerializedName("embedded_file")
        @Schema(name = "embedded_file", description = "文档中发现的 /EmbeddedFile 标签数量，嵌入文件使 PDF 文件自包含，因为它允许将 PDF 及其引用的文件作为单个实体进行处理")
        Integer embeddedFile,
        @Schema(description = "文档是否加密，由 /Encrypt 标签定义")
        Integer encrypted,
        @Schema(description = "PDF 中发现的 /RichMedia 标签数量，此标签允许在 PDF 文件中附加 Flash 应用程序、音频、视频和其他多媒体")
        Integer flash,
        @Schema(description = "PDF 版本（例如 %PDF-1.7）")
        String header,
        @Schema(description = "PDF 中发现的 /JavaScript 标签数量，此标签用于定义 Javascript 操作，它必须与 /S 标签一起使用以指定操作类型")
        Integer javascript,
        @SerializedName("jbig2_compression")
        @Schema(name = "jbig2_compression", description = "PDF 中发现的 /JBIG2Decode 标签数量，此标签用于解压缩使用 JBIG2 标准编码的数据，还原原始单色（每像素 1 位）图像数据")
        Integer jbig2Compression,
        @Schema(description = "PDF 中发现的 /JS 标签数量，此标签与 /JavaScript 标签一起使用，在定义对象时添加内联 javascript 代码。通常情况下，js 和 javascript 的值应该相同")
        Integer js,
        @SerializedName("num_endobj")
        @Schema(name = "num_endobj", description = "对象定义的数量（endobj 关键字），该值应与 num_obj 字段相同")
        Integer numEndobj,
        @SerializedName("num_endstream")
        @Schema(name = "num_endstream", description = "定义的流对象数量（endstream 关键字），该值应与 num_stream 字段相同")
        Integer numEndstream,
        @SerializedName("num_launch_actions")
        @Schema(name = "num_launch_actions", description = "PDF 中发现的 /Launch 标签数量，此标签定义了一个启动操作，用于启动应用程序、打开或打印文档")
        Integer numLaunchActions,
        @SerializedName("num_obj")
        @Schema(name = "num_obj", description = "对象定义的数量（obj 关键字）")
        Integer numObj,
        @SerializedName("num_object_streams")
        @Schema(name = "num_object_streams", description = "对象流的数量，对象流是包含一系列 PDF 对象的流")
        Integer numObjectStreams,
        @SerializedName("num_pages")
        @Schema(name = "num_pages", description = "页数")
        Integer numPages,
        @SerializedName("num_stream")
        @Schema(name = "num_stream", description = "定义的流对象数量（stream 关键字）")
        Integer numStream,
        @Schema(description = "PDF 中发现的 /OpenAction 标签数量，OpenAction 是一个指定在打开文档时应显示的目标或应执行的操作的值")
        Integer openaction,
        @Schema(description = "文档中 startxref 关键字的数量，此关键字用于指示交叉引用表或流的偏移量")
        Integer startxref,
        @SerializedName("suspicious_colors")
        @Schema(name = "suspicious_colors", description = "使用超过 3 个字节表示的颜色数量 (CVE-2009-3459)")
        Integer suspiciousColors,
        @Schema(description = "文档中 trailer 关键字的数量，PDF 的 trailer 使符合标准的阅读器能够快速找到交叉引用表和某些特殊对象")
        Integer trailer,
        @Schema(description = "PDF 中发现的 XFA 标签数量，XFA 代表 Adobe XML 表单架构，支持文档内的交互式表单")
        Integer xfa,
        @Schema(description = "文档中 xref 关键字的数量，该关键字用于定义交叉引用表，其中包含允许随机访问文件内间接对象的信息")
        Integer xref
) {
}