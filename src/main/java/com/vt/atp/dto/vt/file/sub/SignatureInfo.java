package com.vt.atp.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "包含有关 Windows 可执行文件和 Mach-O 文件数字签名的信息，这些信息提取自Windows 可执行文件中的Sigcheck工具和 Mach-O 文件中的 Codesign 命令行实用程序")
public record SignatureInfo(
        //Windows 可执行文件返回的字段
        @Schema(description = "从文件的版本资源中获取（如果找到）")
        String comments,
        @Schema(description = "从文件的版本资源中获取（如果找到）")
        String copyright,
        @SerializedName("counter signers")
        @Schema(name = "counter signers", description = "包含计数器签名者的常用名称。名称之间用字符;分隔")
        String counterSigners,
        @SerializedName("counter signers details")
        @Schema(name = "counter signers details", description = "有关每个计数器签名者证书的详细信息")
        List<signersDetail> counterSignersDetails,
        @Schema(description = "从文件的版本资源中获取（如果找到）")
        String description,
        @SerializedName("file version")
        @Schema(name = "file version", description = "从文件的版本资源中获取（如果找到）")
        String fileVersion,
        @SerializedName("internal name")
        @Schema(name = "internal name", description = "从文件的版本资源中获取（如果找到）")
        String internalName,
        @SerializedName("original name")
        @Schema(name = "original name", description = "从文件的版本资源中获取（如果找到）")
        String originalName,
        @Schema(description = "从文件的版本资源中获取（如果找到）")
        String product,
        @SerializedName("signing date")
        @Schema(name = "signing date", description = "格式为 %H:%M %p %m/%d/%Y")
        String signingDate,
        @Schema(description = "文件中找到的证书列表。列表中的每个项目都是一个SSL 证书对象，仅返回部分字段")
        List<X509> x509,

        //对于 Mach-O、DMG、IPA 和 ZIP 文件，将返回代码设计工具中的字段。这些字段始终是字符串键值对。下列仅为根据VT接口文档列举的常见字段
        @SerializedName("Authority")
        @Schema(name = "Authority", description = "CA 授权机构")
        String authority,
        @SerializedName("CDHash")
        @Schema(name = "CDHash", description = "代码目录哈希值。此代码允许系统验证二进制文件的内容自代码签名以来是否已更改")
        String cdHash,
        @SerializedName("CMSDigest")
        @Schema(name = "CMSDigest", description = " 加密消息语法摘要")
        String cmsDigest,
        @SerializedName("CMSDigestType")
        @Schema(name = "CMSDigestType", description = "CMS摘要类型")
        String cmsDigestType,
        @SerializedName("CandidateCDHash sha256")
        @Schema(name = "CandidateCDHash sha256", description = "CandidateCDHash sha256")
        String candidateCDHashSha256,
        @SerializedName("CandidateCDHashFull sha256")
        @Schema(name = "CandidateCDHashFull sha256", description = "CandidateCDHashFull sha256")
        String candidateCDHashFullSha256,
        @SerializedName("Format")
        @Schema(name = "Format", description = "文件类型")
        String format,
        @SerializedName("Identifier")
        @Schema(name = "Identifier", description = "应用程序ID")
        String identifier,
        @SerializedName("Info.plist")
        @Schema(name = "Info.plist", description = "info.plist 文件")
        String infoPlist,
        @SerializedName("Page size")
        @Schema(name = "Page size", description = "证书页面大小")
        String pageSize,
        @SerializedName("Signature size")
        @Schema(name = "Signature size", description = "签名的大小（以字节为单位）")
        String signatureSize,
        @SerializedName("TeamIdentifier")
        @Schema(name = "TeamIdentifier", description = "开发可执行文件的团队的 ID")
        String teamIdentifier,
        @SerializedName("Timestamp")
        @Schema(name = "Timestamp", description = "证书生成日期，格式为。%b %d, %Y at %H:%M:%S %p")
        String timestamp,

        //都会返回的字段
        @Schema(description = "包含签名者通用名称的字符串。以字符;分隔")
        String signers,
        @SerializedName("signers details")
        @Schema(name = "singers details", description = "包含有关每个签名者证书的详细信息")
        List<signersDetail> singersDetails,
        @Schema(description = "证书状态。可以是“已签名”、“未签名”，或者如果签名存在任何问题，则会在此处注明（例如“证书已被其颁发者明确撤销。”）")
        String verified
) {
    @Schema(description = "证书的详细信息")
    public record signersDetail(
            @Schema(description = "用于创建密钥对的字符串")
            String algorithm,
            @SerializedName("cert issuer")
            @Schema(name = "cert issuer", description = "颁发证书的公司")
            String certIssuer,
            @Schema(description = "证书主题")
            String name,
            @SerializedName("serial number")
            @Schema(name = "serial number", description = "以十六进制形式，逐字节用空格分隔")
            String serialNumber,
            @Schema(description = "可以表示“有效”，或者说明证书存在的问题（例如“此证书或证书链中的某个证书已过期”）")
            String status,
            @Schema(description = "证书哈希的十六进制表示形式")
            String thumbprint,
            @SerializedName("valid from")
            @Schema(name = "valid from", description = "有效期开始日期，格式为 %H:%M %p %m/%d/%Y")
            String validFrom,
            @SerializedName("valid to")
            @Schema(name = "valid to", description = "到期日期，格式为 %H:%M %p %m/%d/%Y")
            String validTo,
            @SerializedName("valid usage")
            @Schema(name = "valid usage", description = "指示证书适用于哪些情况（即“代码签名”）")
            String validUsage
    ){}

    @Schema(description = "SSL 证书部分信息")
    public record X509(
            @Schema(description = "用于创建密钥对的字符串")
            String algorithm,
            @SerializedName("cert issuer")
            @Schema(name = "cert issuer", description = "颁发证书的公司。提取自证书的字段")
            String certIssuer,
            @Schema(description = "证书主题")
            String name,
            @SerializedName("serial number")
            @Schema(name = "serial number", description = "以十六进制形式，逐字节用空格分隔")
            String serialNumber,
            @Schema(description = "证书哈希的十六进制表示形式")
            String thumbprint,
            @SerializedName("valid from")
            @Schema(name = "valid from", description = "有效期开始日期，格式为 %H:%M %p %m/%d/%Y")
            String validFrom,
            @SerializedName("valid to")
            @Schema(name = "valid to", description = "到期日期，格式为 %H:%M %p %m/%d/%Y")
            String validTo,
            @SerializedName("valid usage")
            @Schema(name = "valid usage", description = "指示证书的有效范围（例如“代码签名”）。提取自“扩展密钥用法”证书扩展")
            String validUsage,
            @SerializedName("thumbprint_sha256")
            @Schema(name = "thumbprint_sha256", description = "SHA‑256 指纹")
            String thumbprintSha256,
            @SerializedName("thumbprint_md5")
            @Schema(name = "thumbprint_md5", description = "MD5 指纹")
            String thumbprintMd5
    ){}
}
