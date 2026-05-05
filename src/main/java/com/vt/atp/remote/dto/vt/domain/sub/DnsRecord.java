package com.vt.atp.remote.dto.vt.domain.sub;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "dns记录")
public record DnsRecord(
        @Schema(description = "过期时间 (Expire Limit)。如果辅 DNS 服务器持续无法联系主服务器，超过此时间后，其区域数据将被视为无效，不再权威应答")
        Long expire,
        @Schema(description = "DNS 标志位，其含义与记录的 type 有关。例如在 CAA 记录中，flag 为 0 表示非关键（non-critical），为 128 表示关键（critical）")
        Integer flag,
        @Schema(description = "最小 TTL (Minimum TTL)。在 SOA 记录中，它有两个作用：(1) 作为区域内所有记录的默认 TTL；(2) 用于否定缓存 (Negative Caching)，即规定“域名不存在”等错误信息的缓存时间")
        Long minimum,
        @Schema(description = "优先级 (Priority)。值越小，优先级越高。主要用于 MX 和 SRV 记录，供客户端优先选择服务器")
        Integer priority,
        @Schema(description = "重试间隔 (Retry Interval)。如果辅 DNS 服务器刷新失败，它会在等待这个时间后重试")
        Integer refresh,
        @Schema(description = "负责人邮箱 (Responsible Name)。区域负责人的邮箱地址，格式中将 @ 符号替换为了 .。例如 admin.example.com 代表 admin@example.com")
        String rname,
        @Schema(description = "刷新间隔 (Refresh Interval)。辅 DNS 服务器检查主 DNS 服务器序列号以获取更新的时间间隔")
        Long retry,
        @Schema(description = "序列号 (Serial Number)。区域文件的版本号，当区域数据更新时，此值必须递增，以便辅 DNS 服务器判断是否需要同步")
        Integer serial,
        @Schema(description = "通常与 type: \"CAA\" (证书颁发机构授权) 记录绑定，用于指定要限制的属性，例如 issue (授权颁发) 或 issuewild (授权通配符证书)")
        String tag,
        @Schema(description = "规定该条 DNS 记录在 DNS 解析器上的缓存时间长度，单位为秒。超过此时间，解析器会重新查询")
        Long ttl,
        @Schema(description = "DNS 记录类型，例如 A, SOA, MX, NS 等")
        String type,
        @Schema(description = "DNS 记录的值，例如 A 记录的 IPv4 地址或 MX 记录的邮件服务器地址")
        String value
) {
}
