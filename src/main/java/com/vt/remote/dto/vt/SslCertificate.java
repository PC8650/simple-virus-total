package com.vt.remote.dto.vt;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "ssl证书信息。https://docs.virustotal.com/reference/ssl-certificate")
public record SslCertificate(
        @SerializedName("cert_signature")
        @Schema(name = "cert_signature", description = "证书签名和算法")
        CertSignature certSignature,
        @Schema(description = "所有证书扩展的词典。包括常见的扩展，子领域可能有所不同")
        Extension extensions,
        @SerializedName("first_seen_date")
        @Schema(name = "first_seen_date", description = "证书首次被VirusTotal检索的日期时间戳")
        Long firstSeenDate,
        @Schema(description = "证书发行者数据")
        GeneralInformation issuer,
        @SerializedName("public_key")
        @Schema(name = "public_key", description = "公钥信息")
        PublicKey publicKey,
        @SerializedName("serial_number")
        @Schema(name = "serial_number", description = "十六进制证书序列号")
        String serialNumber,
        @SerializedName("signature_algorithm")
        @Schema(name = "signature_algorithm", description = "用于签名的算法（即“sha1RSA”）")
        String signatureAlgorithm,
        @Schema(description = "证书内容长度")
        Integer size,
        @Schema(description = "证书主体数据")
        GeneralInformation subject,
        @Schema(description = "证书内容 SHA1 哈希")
        String thumbprint,
        @SerializedName("thumbprint_sha256")
        @Schema(name = "thumbprint_sha256", description = "证书内容 SHA256 哈希")
        String thumbprintSha256,
        @Schema(description = "有效期")
        Validity validity,
        @Schema(description = "证书版本（通常称为“V1”、“V2”或“V3”）")
        String version
) {

    @Schema(description = "证书签名和算法")
    public record CertSignature(
            @Schema(description = "签名特征十六进制转储")
            String signature,
            @SerializedName("signature_algorithm")
            @Schema(name = "signature_algorithm", description = "使用的算法（即“sha256RSA”）")
            String signatureAlgorithm
    ){}

    @Schema(description = "场景证书扩展。只包含部分常见字段")
    public record Extension(
            @SerializedName("CA")
            @Schema(name = "CA", description = "该证书主体是否充当证书颁发机构 (CA)")
            Boolean ca,
            @SerializedName("subject_key_identifier")
            @Schema(name = "subject_key_identifier", description = "识别被认证的公钥")
            String subjectKeyIdentifier,
            @SerializedName("authority_key_identifier")
            @Schema(name = "authority_key_identifier", description = "识别用于验证该证书或CRL签名的公钥")
            AuthorityKeyIdentifier authorityKeyIdentifier,
            @SerializedName("key_usage")
            @Schema(name = "key_usage", description = "认证公钥的使用目的")
            List<String> keyUsage,
            @SerializedName("ca_information_access")
            @Schema(name = "ca_information_access", description = """
                    权威信息访问位置是添加到证书的权威信息访问扩展中的URL
                    如 CA Issuers， OCSP 等
                    """)
            Map<String, String> caInformationAccess,
            @SerializedName("crl_distribution_points")
            @Schema(name = "crl_distribution_points", description = "标识证书用户应查证证书是否被撤销的CRL分发点")
            List<String> crlDistributionPoints,
            @SerializedName("extended_key_usage")
            @Schema(name = "extended_key_usage", description = "表示认证公钥可用于一个或多个用途，作为key_usage扩展字段中基本目的的补充或替代")
            List<String> extendedKeyUsage,
            @SerializedName("subject_alternative_name")
            @Schema(name = "subject_alternative_name", description = "一个或多个备用名称，使用各种名称形式，用于由CA绑定到认证公钥的实体")
            List<String> subjectAlternativeName,
            @SerializedName("certificate_policies")
            @Schema(name = "certificate_policies", description = "不同的证书策略会关联到可能使用认证密钥的不同应用程序")
            List<String> certificatePolicies,
            @SerializedName("netscape_cert_comment")
            @Schema(name = "netscape_cert_comment", description = "曾在证书中包含自由形式的文本注释")
            String netscapeCertComment,
            @SerializedName("cert_template_name_dc")
            @Schema(name = "cert_template_name_dc", description = "BMP数据值“域控制器”，见微软Q291010")
            String certTemplateNameDc,
            @SerializedName("netscape_certificate")
            @Schema(description = "netscape_certificate", defaultValue = "识别证书主体是SSL客户端、SSL服务器还是CA")
            Boolean netscapeCertificate,
            @SerializedName("pe_logotype")
            @Schema(name = "pe_logotype", description = "证书是否包含标志字体")
            Boolean peLogotype,
            @SerializedName("old_authority_key_identifier")
            @Schema(name = "old_authority_key_identifier", description = "证书是否拥有旧的权威密钥标识符扩展名")
            Boolean oldAuthorityKeyIdentifier
    ){
        @Schema(description = "识别用于验证该证书或CRL签名的公钥")
        public record AuthorityKeyIdentifier(
                @Schema(description = "key hexdump")
                String keyid,
                @SerializedName("serial_number")
                @Schema(name = "serial_number", description = "serial number hexdump")
                String serialNumber
        ){}
    }

    @Schema(description = "通用信息")
    public record GeneralInformation(
            @Schema(description = "国家名称")
            String C,
            @Schema(description = "通用名")
            String CN,
            @Schema(description = "地方性")
            String L,
            @Schema(description = "组织")
            String O,
            @Schema(description = "组织单位")
            String OU,
            @Schema(description = "州名或省名")
            String ST
    ){}

    @Schema(description = "公钥信息")
    public record PublicKey(
            @Schema(description = "“RSA”、“DSA”或“EC”中的任何一个。表示用于生成证书的算法")
            String algorithm,
            @Schema(description = "RSA 信息。如果 algorithm 是 RSA")
            Rsa rsa,
            @Schema(description = "DSA 信息。如果 algorithm 是 DSA")
            Dsa dsa,
            @Schema(description = "EC 信息。如果 algorithm 是 EC")
            Ec ec
    ){
        public record Rsa(
                @SerializedName("key_size")
                @Schema(name = "key_size", description = "长度")
                Integer keySize,
                @Schema(description = "modulus hexdump")
                String modulus,
                @Schema(description = "exponent hexdump")
                String exponent
        ){}
        public record Dsa(
                @Schema(description = "p component hexdump")
                String p,
                @Schema(description = "q component hexdump")
                String q,
                @Schema(description = "g component hexdump")
                String g,
                @Schema(description = "public key hexdump")
                String pub
        ){}
        public record Ec(
                @Schema(description = "曲线名称")
                String oid,
                @Schema(description = "public key  hexdump.")
                String pub
        ){}
    }

    @Schema(description = "有效性")
    public record Validity(
            @SerializedName("not_after")
            @Schema(name = "not_after", description = "有效期。%Y-%m-%d %H:%M:%S")
            String not_after,
            @SerializedName("not_before")
            @Schema(name = "not_before", description = "发行日期。%Y-%m-%d %H:%M:%S")
            String not_before
    ){}
}
