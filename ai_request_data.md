# AI 请求数据说明（ReportContent）

本文档说明 `SummaryAdvisor` 送入大模型的 JSON 载荷结构，并提供与当前 DTO 建模一致的 mock；第二节列出各 `*_skill.md` 中引用的 JSON 字段**完整路径**（路径均相对于 `ReportContent` 根对象）。

---

## 0. SummaryAdvisor 数据流

```
ReportAdvisor
  ├─ report  ← scanner.getReport(reportId).getData()     // FileReportResp | UrlReportResp | IpReportResp | DomainReportResp
  ├─ behaviour ← FILE 专用：List<FileBehaviourReportResp>（可能为 null）
  ├─ mitre   ← FILE 专用：Map<沙箱名, FileMitreResp>（可能为 null）
  ├─ type    ← TypeEnum（FILE / URL / IP / DOMAIN）
  └─ url     ← type.guiUrl(reportId)，VT GUI 链接

SummaryAdvisor
  ├─ contentJson = gson.toJson(reportContent)   // 写入 User 提示词「数据载荷」
  ├─ systemPrompt = skill 正文 [+ externalSkill 追加]
  └─ userPrompt = 任务说明 + 可选 description + contentJson
```

- **序列化**：默认 Gson（`serializeNulls()`），Java 字段名为主；带 `@SerializedName` 的字段以注解名为 JSON 键（如 `meaningful_name`）。
- **URL / IP / DOMAIN**：`behaviour`、`mitre` 恒为 `null`（`ReportAdvisor` 仅在 `TypeEnum.FILE` 时拉取）。
- **外部 Skill 匹配**：对整段 `contentJson` 做 tag 词边界匹配，与 Skill 手册中的路径无直接耦合。

---

## 1. ReportContent 带注释 Mock

### 1.1 FILE

`report` → `FileReportResp`；`behaviour` → `List<FileBehaviourReportResp>`；`mitre` → `Map<String, FileMitreResp>`（键为沙箱名，如 `CAPA`）。

```jsonc
{
  "type": "FILE",                    // TypeEnum 名称
  "url": "https://www.virustotal.com/gui/file/5f3b4c6a...",  // GUI 链接，非原始 URL
  "error": null,                     // 预留；正常流程为 null
  "report": {
    "id": "5f3b4c6ac499dff96fbf537708792880c031fabbdd8f580ec2830178184d169b",
    "type": "file",
    "links": { "self": "..." },
    "attributes": {
      "md5": "...",
      "sha1": "...",
      "sha256": "5f3b4c6a...",
      "size": 3526656,
      "meaningful_name": "sample.exe",
      "names": ["sample.exe"],
      "first_submission_date": 1710000000,
      "last_submission_date": 1715000000,
      "last_analysis_date": 1716000000,
      "last_modification_date": 1716000000,
      "times_submitted": 3,
      "unique_sources": 2,
      "reputation": 0,
      "tags": ["peexe", "overlay"],
      "type_tags": ["executable", "windows", "pe"],
      "last_analysis_stats": {
        "malicious": 12,
        "suspicious": 2,
        "undetected": 55,
        "harmless": 0,
        "failure": 0,
        "timeout": 0,
        "confirmed-timeout": 0,
        "type-unsupported": 0
      },
      "last_analysis_results": {
        "Kaspersky": {
          "method": "blacklist",
          "engine_name": "Kaspersky",
          "category": "malicious",
          "result": "Trojan.Win32.Example"
        }
      },
      "threat_verdict": "VERDICT_MALICIOUS",
      "crowdsourced_ai_results": [],
      "crowdsourced_ids_stats": {
        "high": 2,
        "medium": 0,
        "low": 0,
        "info": 0
      },
      "crowdsourced_ids_results": [
        {
          "alert_severity": "high",
          "rule_msg": "ET TROJAN...",
          "rule_category": "A Network Trojan was detected",
          "rule_id": "2012345",
          "rule_source": "EmergingThreats",
          "alert_context": [
            {
              "src_ip": "192.168.1.2",
              "src_port": 50000,
              "dest_ip": "1.2.3.4",
              "dest_port": 80,
              "protocol": "TCP",
              "hostname": "evil.example",
              "url": "http://evil.example/payload.exe"
            }
          ]
        }
      ],
      "crowdsourced_yara_results": [
        {
          "rule_name": "suspicious_packer",
          "author": "Alice",
          "description": "Detects packed executable",
          "ruleset_name": "Packers",
          "ruleset_id": "01234",
          "source": "Github",
          "match_in_subfile": false
        }
      ],
      "known_distributors": {
        "distributors": ["Microsoft Corporation"],
        "products": ["Windows OS"],
        "filenames": ["cmd.exe"],
        "links": [],
        "data_sources": ["NSRL"]
      },
      "popular_threat_classification": {
        "suggested_threat_label": "trojan.win32/emotet",
        "popular_threat_category": [{ "value": "trojan", "count": 25 }],
        "popular_threat_name": [{ "value": "emotet", "count": 15 }]
      },
      "link_info": {
        "target_path": "C:\\Windows\\System32\\cmd.exe",
        "command_line_arguments": "/c powershell.exe -w hidden -enc ...",
        "working_directory": "C:\\Windows\\System32",
        "creation_date": 1600000000,
        "modification_date": 1600000000,
        "access_date": 1600000000,
        "mac_address": "00:11:22:33:44:55",
        "machine_id": "desktop-abc",
        "volume_serial_number": "1234-5678",
        "volume_label": "System",
        "extra_data": { "dlt_properties": {} }
      },
      "vba_info": {
        "strings": ["AutoOpen", "Shell"],
        "deobfuscated_strings": []
      },
      "pdf_info": {
        "javascript": 1,
        "js": 1,
        "openaction": true,
        "num_launch_actions": 0,
        "embedded_file": 0,
        "encrypted": false,
        "flash": 0,
        "xfa": 0,
        "num_obj": 15,
        "num_stream": 5
      },
      "powershell_info": {
        "cmdlets": ["Invoke-Expression"],
        "cmdlets_alias": ["iex"],
        "dotnet_calls": ["System.Net.WebClient"],
        "functions": ["DecryptPayload"],
        "ps_variables": ["payload"]
      },
      "html_info": {
        "title": "Welcome",
        "hrefs": ["http://evil.example"],
        "iframes": [],
        "meta": [{ "name": "description", "content": "..." }],
        "scripts": [],
        "trackers": []
      },
      "javascript_info": {
        "tags": ["eval", "unescape", "obfuscated"]
      },
      "class_info": {
        "name": "com/example/Main",
        "extend": "java/lang/Object",
        "implement": [],
        "methods": [],
        "provides": [],
        "requires": [],
        "constants": ["http://evil.example/api"]
      },
      "jar_info": {
        "filenames": ["META-INF/MANIFEST.MF", "com/example/Main.class"],
        "files_by_type": { "class": 1, "MF": 1 },
        "manifest": "Manifest-Version: 1.0\r\n...",
        "strings": [],
        "packages": ["com/example"]
      },
      "elf_info": {
        "header": { "machine": "Advanced Micro Devices X86-64", "entrypoint": 4194304, "os_abi": "UNIX - System V" },
        "import_list": [{ "library_name": "libc.so.6" }],
        "export_list": [],
        "shared_libraries": ["libc.so.6"],
        "packers": ["UPX"],
        "section_list": [],
        "segment_list": []
      },
      "androguard": {
        "packages": ["com.example.app"],
        "main_activity": "com.example.app.MainActivity",
        "android_version_code": "1",
        "android_version_name": "1.0",
        "min_sdk_version": "21",
        "target_sdk_version": "33",
        "activities": [],
        "services": [],
        "receivers": [],
        "providers": [],
        "permission_details": {
          "android.permission.INTERNET": {
            "permission_type": "normal",
            "description": "..."
          }
        },
        "risk_indicator": {
          "APK": { "Count": 1 },
          "PERM": { "Count": 5 }
        },
        "certificate": { "issuer": "...", "subject": "..." },
        "strings_information": ["http://c2.example/"]
      },
      "ipa_info": {
        "apps": [{ "commands": [], "libs": [], "segments": [] }],
        "itunes": {},
        "plist": {
          "CFBundleIdentifier": "com.example.ios",
          "MinimumOSVersion": "12.0"
        },
        "provision": {
          "ExpirationDate": "...",
          "Entitlements": { "get-task-allow": true }
        }
      },
      "dmg_info": {
        "dmg_version": "1.0",
        "blkx": [],
        "gpt": { "partitions": [] },
        "hfs": {
          "volume_data": {},
          "num_files": 100,
          "main_executable": {},
          "info_plist": {}
        }
      },
      "traffic_inspection": {
        "http": [
          {
            "url": "http://evil.example/payload.exe",
            "remote_host": "1.2.3.4:80",
            "method": "GET",
            "response_code": 200,
            "userAgent": "Mozilla/5.0...",
            "binary_hash": "...",
            "binary_magic": "MZ"
          }
        ]
      },
      "suricata": {
        "ET MALWARE": { "suricata_alerts": ["ET TROJAN..."] }
      },
      "wireshark": {
        "dns": [{ "query": "evil.example", "answers": ["1.2.3.4"] }],
        "pcap": {
          "captureDuration": "10.5",
          "dataSize": "1024",
          "numberOfPackets": 15
        }
      },
      "bundle_info": {
        "type": "ZIP",
        "num_children": 4,
        "extensions": { "exe": 1, "txt": 3 },
        "file_types": { "Win32 EXE": 1, "ASCII text": 3 },
        "uncompressed_size": 10485760,
        "highest_datetime": "2024-01-01 00:00:00",
        "lowest_datetime": "2020-01-01 00:00:00",
        "beginning": "MZ...",
        "error": null
      },
      "pe_info": {
        "imphash": "...",
        "entry_point": 4096,
        "timestamp": 1609459200,
        "sections": [{ "name": ".text", "entropy": 6.1 }],
        "overlay": { "size": 10336 },
        "resource_details": [],
        "import_list": [{ "library_name": "kernel32.dll" }],
        "exports": []
      },
      "signature_info": {
        "verified": "Signed",
        "signers": "Example Corp",
        "signers details": [{ "status": "Valid", "thumbprint": "..." }],
        "counter signers details": [{ "status": "Valid", "thumbprint": "..." }],
        "x509": [{ "thumbprint": "...", "thumbprint_sha256": "...", "thumbprint_md5": "..." }]
      }
    }
  },
  "behaviour": [
    {
      "id": "5f3b4c6a_CAPA",
      "type": "file_behaviour",
      "attributes": {
        "sandbox_name": "CAPA",
        "registry_keys_set": [{ "key": "HKCU\\...", "value": "..." }],
        "services_created": [],
        "services_started": [],
        "command_executions": ["cmd.exe /c ..."],
        "processes_injected": [],
        "windows_hidden": [],
        "windows_searched": [],
        "mutexes_created": ["Global\\..."],
        "signals_observed": [],
        "invokes": [],
        "files_written": ["C:\\Users\\..."],
        "files_deleted": [],
        "files_opened": [],
        "signals_hooked": [],
        "calls_highlighted": ["GetKeyState"],
        "crypto_algorithms_observed": ["AES"],
        "crypto_keys": []
      }
    }
  ],
  "mitre": {
    "CAPA": {
      "tactics": [
        {
          "id": "TA0007",
          "name": "Discovery",
          "link": "https://attack.mitre.org/tactics/TA0007/",
          "description": "...",
          "techniques": [
            {
              "id": "T1082",
              "name": "System Information Discovery",
              "link": "https://attack.mitre.org/techniques/T1082/",
              "description": "...",
              "signatures": [
                {
                  "severity": "INFO",
                  "description": "get system information on Windows",
                  "match_data": ["api: GetSystemInfo"]
                }
              ]
            }
          ]
        }
      ]
    },
    "CAPE Sandbox": { "tactics": [] }
  }
}
```

> `behaviour` 为数组，元素结构见 `FileBehaviourReportResp`；建模字段多于 Skill 列举项，Skill 未提及的字段不会出现在第二节路径表中。

---

### 1.2 URL

`behaviour`、`mitre` 为 `null`。

```jsonc
{
  "type": "URL",
  "url": "https://www.virustotal.com/gui/url/abc123...",
  "error": null,
  "report": {
    "id": "<url-sha256-id>",
    "type": "url",
    "attributes": {
      "url": "https://evil.example/phish",
      "last_analysis_stats": {
        "malicious": 5,
        "suspicious": 1,
        "undetected": 60,
        "harmless": 0
      },
      "last_analysis_results": {
        "Phishtank": {
          "engine_name": "Phishtank",
          "method": "blacklist",
          "category": "malicious",
          "result": "phishing"
        }
      },
      "last_final_url": "https://landing.evil.example/",
      "redirection_chain": ["https://evil.example/phish"],
      "last_http_response_code": 200,
      "last_http_response_headers": { "content-type": "text/html" },
      "last_http_response_content_length": 12345,
      "last_http_response_content_sha256": "...",
      "last_http_response_cookies": {},
      "html_meta": { "title": ["Fake Login"] },
      "title": "Sign in",
      "has_content": true,
      "reputation": -50,
      "total_votes": { "harmless": 0, "malicious": 3 },
      "categories": { "Forcepoint": "phishing" },
      "tags": ["phishing"],
      "tld": "example",
      "targeted_brand": { "engine": "BrandX" },
      "favicon": { "raw_md5": "...", "dhash": "..." },
      "trackers": { "GoogleAnalytics": [{ "id": "UA-000000", "timestamp": 1715000000, "url": "..." }] },
      "outgoing_links": ["https://other.example/"],
      "first_submission_date": 1710000000,
      "last_submission_date": 1715000000,
      "times_submitted": 2,
      "last_analysis_date": 1716000000,
      "last_modification_date": 1716000000
    }
  },
  "behaviour": null,
  "mitre": null
}
```

---

### 1.3 IP

```jsonc
{
  "type": "IP",
  "url": "https://www.virustotal.com/gui/ip-address/1.2.3.4",
  "error": null,
  "report": {
    "id": "1.2.3.4",
    "type": "ip_address",
    "attributes": {
      "last_analysis_stats": { "malicious": 2, "suspicious": 0, "undetected": 70, "harmless": 0 },
      "last_analysis_date": 1716000000,
      "last_modification_date": 1716000000,
      "last_analysis_results": {
        "AlienVault": {
          "engine_name": "AlienVault",
          "method": "heuristic",
          "category": "malicious",
          "result": "malicious"
        }
      },
      "asn": 12345,
      "as_owner": "Example ISP",
      "network": "1.2.3.0/24",
      "continent": "AS",
      "country": "CN",
      "regional_internet_registry": "APNIC",
      "last_https_certificate": {
        "subject": { "CN": "bad.example" },
        "issuer": { "CN": "Let's Encrypt" },
        "validity": { "not_before": "2026-01-01T00:00:00Z", "not_after": "2026-12-31T23:59:59Z" },
        "first_seen_date": 1710000000,
        "thumbprint_sha256": "...",
        "signature_algorithm": "sha256RSA",
        "public_key": { "algorithm": "RSA", "rsa": { "key_size": 2048 } },
        "extensions": {
          "key_usage": ["digitalSignature"],
          "extended_key_usage": ["serverAuth"],
          "subject_alternative_name": ["bad.example", "*.bad.example"]
        }
      },
      "last_https_certificate_date": 1716000000,
      "jarm": "27d40d...",
      "reputation": -10,
      "total_votes": { "harmless": 1, "malicious": 2 },
      "tags": ["c2"],
      "whois": "inetnum: 1.2.3.0 - 1.2.3.255 ...",
      "whois_date": 1710000000
    }
  },
  "behaviour": null,
  "mitre": null
}
```

---

### 1.4 DOMAIN

```jsonc
{
  "type": "DOMAIN",
  "url": "https://www.virustotal.com/gui/domain/evil.example",
  "error": null,
  "report": {
    "id": "evil.example",
    "type": "domain",
    "attributes": {
      "last_analysis_stats": { "malicious": 4, "suspicious": 1, "undetected": 65, "harmless": 0 },
      "last_analysis_date": 1716000000,
      "last_modification_date": 1716000000,
      "last_analysis_results": {
        "alphaMountain": {
          "engine_name": "alphaMountain",
          "method": "blacklist",
          "category": "malicious",
          "result": "malware"
        }
      },
      "categories": { "alphaMountain": "malware" },
      "tags": ["dga"],
      "tld": "example",
      "creation_date": 1700000000,
      "expiration_date": 1800000000,
      "last_update_date": 1710000000,
      "registrar": "Example Registrar",
      "whois": "Domain Name: evil.example ...",
      "whois_date": 1710000000,
      "last_dns_records": [
        { "type": "A", "value": "1.2.3.4", "ttl": 60 },
        { "type": "MX", "value": "mail.evil.example", "ttl": 300, "priority": 10 },
        { "type": "CAA", "value": "letsencrypt.org", "ttl": 300, "flag": 0, "tag": "issue" },
        { "type": "SOA", "value": "ns1.evil.example", "ttl": 300, "serial": 2024010101, "refresh": 3600, "retry": 600, "expire": 604800, "minimum": 300, "rname": "admin.evil.example" }
      ],
      "last_dns_records_date": 1715000000,
      "last_https_certificate": {
        "subject": { "CN": "evil.example" },
        "issuer": { "O": "CA Corp" },
        "validity": { "not_before": "2025-06-01", "not_after": "2026-06-01" },
        "first_seen_date": 1710000000,
        "thumbprint_sha256": "...",
        "signature_algorithm": "sha256RSA",
        "public_key": { "algorithm": "RSA", "rsa": { "key_size": 2048 } },
        "extensions": {
          "key_usage": ["digitalSignature"],
          "extended_key_usage": ["serverAuth"],
          "subject_alternative_name": ["evil.example"]
        }
      },
      "last_https_certificate_date": 1715000000,
      "jarm": "29ad3b...",
      "popularity_ranks": {
        "Alexa": { "rank": 1000000, "timestamp": 1710000000 }
      },
      "reputation": -5,
      "total_votes": { "harmless": 0, "malicious": 1 },
      "crowdsourced_context": [
        { "title": "...", "severity": "high", "details": "...", "source": "...", "timestamp": 1710000000 }
      ],
      "favicon": { "raw_md5": "...", "dhash": "..." }
    }
  },
  "behaviour": null,
  "mitre": null
}
```

---

## 2. Skill 字段路径对照（`resources/skills/*_skill.md`）

说明：

- 路径前缀均为 **`ReportContent` 根**；Skill 文中简写的 `id` / `type` 若指 VT 对象，在本项目中对应 **`report.id` / `report.type`**。
- `behaviour[i]`、`mitre.<沙箱名>` 为 Skill 记号；JSON 中分别为数组下标与 Map 键。
- `last_analysis_results` 在模型中为 **Map**（键为引擎名）；Skill 中的 `engine_name` 等指 **Map 的 value** 字段。
- 下列仅列 Skill **字典/算法/输出规范**中显式出现的路径，不含 Skill 输出章节中的叙述性占位（如 `{malicious}`）。
- 表头 **含义**：字段在分析中的业务含义；**备注**：路径写法或与 DTO 的差异说明。

### 2.1 `file_skill.md`（`SkillEnum.FILE`）

| 完整路径 | 含义 | 备注 |
| :--- | :--- | :--- |
| `report.id` | VT 文件对象 ID（通常为 SHA256） | Skill 写作 `id` |
| `report.type` | VT 对象类型字符串 | 值如 `file` |
| `url` | VirusTotal GUI 报告页链接 | 根节点字段 |
| `report.attributes.md5` | 文件 MD5 哈希 | |
| `report.attributes.sha1` | 文件 SHA1 哈希 | |
| `report.attributes.sha256` | 文件 SHA256 哈希 | |
| `report.attributes.size` | 文件大小（字节） | |
| `report.attributes.meaningful_name` | 最具代表性的文件名 | 输出规范「文件名」 |
| `report.attributes.names` | 已知别名列表 | |
| `report.attributes.first_submission_date` | 首次提交 VT 时间（UTC 时间戳） | |
| `report.attributes.last_submission_date` | 最近提交 VT 时间 | |
| `report.attributes.times_submitted` | 累计提交次数 | |
| `report.attributes.unique_sources` | 不同来源提交数 | |
| `report.attributes.reputation` | 社区信誉分（负值表示恶评居多） | |
| `report.attributes.tags` | 静态标签（如 packed、peexe） | |
| `report.attributes.type_tags` | 文件类型相关标签 | |
| `report.attributes.last_analysis_date` | 最近扫描时间 | 用于判断数据新鲜度 |
| `report.attributes.last_modification_date` | VT 对象最后修改时间 | |
| `report.attributes.last_analysis_stats` | 各引擎检出数量汇总 | |
| `report.attributes.last_analysis_stats.malicious` | 判定为恶意的引擎数 | |
| `report.attributes.last_analysis_stats.suspicious` | 判定为可疑的引擎数 | |
| `report.attributes.last_analysis_stats.undetected` | 未检出的引擎数 | |
| `report.attributes.last_analysis_stats.harmless` | 判定为无害的引擎数 | |
| `report.attributes.last_analysis_stats.failure` | 扫描失败的引擎数 | |
| `report.attributes.last_analysis_stats.timeout` | 扫描超时的引擎数 | |
| `report.attributes.last_analysis_results` | 各引擎详细结果 | Map，键为引擎名 |
| `report.attributes.last_analysis_results.*.engine_name` | 引擎名称 | `*` 为 Map 键 |
| `report.attributes.last_analysis_results.*.method` | 检测方法（如 blacklist、heuristic） | |
| `report.attributes.last_analysis_results.*.result` | 检出名称/描述 | |
| `report.attributes.last_analysis_results.*.category` | 结果分类（malicious/suspicious 等） | |
| `report.attributes.threat_verdict` | VT 官方威胁结论 | 如 VERDICT_MALICIOUS |
| `report.attributes.crowdsourced_ai_results` | 众包 AI 分析摘要 | |
| `report.attributes.crowdsourced_ids_stats` | IDS 警报按严重等级统计 | |
| `report.attributes.crowdsourced_ids_results` | 匹配的入侵检测系统警报明细 | List |
| `report.attributes.crowdsourced_yara_results` | 命中的众包 YARA 规则明细 | List |
| `report.attributes.known_distributors` | 已知软件分发者特征 | |
| `report.attributes.popular_threat_classification` | 业界引擎聚类统计的流行威胁分类 | |
| `report.attributes.link_info` | Windows LNK 快捷方式信息 | 对象含 `target_path` 等 |
| `report.attributes.vba_info` | Office VBA 宏特征 | 对象含 `deobfuscated_strings` 等 |
| `report.attributes.pdf_info` | Adobe PDF 结构与内嵌动作信息 | 对象含 `javascript` 计数等 |
| `report.attributes.powershell_info` | PowerShell 脚本执行特征 | 对象含 `cmdlets` 等 |
| `report.attributes.html_info` | HTML 网页结构信息 | 对象含 `scripts`, `iframes` 等 |
| `report.attributes.javascript_info` | JS 或 PDF 内嵌脚本提取的特征标签 | VT 官方字段；DTO 使用 `@SerializedName("javascript_info")` |
| `report.attributes.javascript_info.tags` | JavaScript 特征标签 | 如 eval、unescape、obfuscated |
| `report.attributes.class_info` | Java Class 字节码结构信息 | 对象含 `name`, `methods` 等 |
| `report.attributes.jar_info` | Java JAR 包结构信息 | 对象含 `filenames`, `files_by_type` 等 |
| `report.attributes.elf_info` | Linux ELF 二进制执行文件结构 | 对象含 `header`, `import_list` 等 |
| `report.attributes.androguard` | Android APK & AXML 结构信息 | 对象含 `activities`, `permission_details` 等 |
| `report.attributes.ipa_info` | iOS App (IPA) 包信息 | 对象含 `plist`, `provision` 等 |
| `report.attributes.dmg_info` | macOS DMG 镜像包信息 | 对象含 `dmg_version`, `hfs` 等 |
| `report.attributes.traffic_inspection` | PCAP 网络抓包（HTTP/Pcap） | 对象含 `http` 列表 |
| `report.attributes.suricata` | PCAP 命中的 Suricata 规则字典 | Map |
| `report.attributes.wireshark` | PCAP 的 DNS 解析及流量统计信息 | 对象含 `dns`, `pcap` |
| `report.attributes.bundle_info` | 压缩归档/捆绑包信息 | 目标为压缩文件时重点分析 |
| `report.attributes.bundle_info.type` | 归档/容器类型 | 与扩展名、type_tags 交叉验证 |
| `report.attributes.bundle_info.num_children` | 归档内文件/目录数量 | |
| `report.attributes.bundle_info.extensions` | 归档内扩展名计数 | Map，需遍历全部 key |
| `report.attributes.bundle_info.file_types` | 归档内文件类型计数 | Map，和扩展名分布对比 |
| `report.attributes.bundle_info.uncompressed_size` | 未压缩内容总大小 | 用于估算压缩比 |
| `report.attributes.bundle_info.highest_datetime` | 归档内最新时间戳 | 元数据异常线索 |
| `report.attributes.bundle_info.lowest_datetime` | 归档内最早时间戳 | |
| `report.attributes.bundle_info.beginning` | 解压后头部/起始字节 | 可辅助识别内部载荷 |
| `report.attributes.bundle_info.error` | 解压或解析错误 | 需记录但不能单独定恶意 |
| `report.attributes.pe_info.imphash` | PE 导入表哈希（家族关联） | |
| `report.attributes.pe_info.entry_point` | 程序入口点偏移 | |
| `report.attributes.pe_info.timestamp` | PE 编译时间戳 | |
| `report.attributes.pe_info.sections` | PE 节区列表（含熵值） | List |
| `report.attributes.pe_info.overlay` | 文件尾部附加数据区 | |
| `report.attributes.pe_info.resource_details` | 内嵌资源信息 | List |
| `report.attributes.pe_info.import_list` | 导入 DLL/函数列表 | List |
| `report.attributes.pe_info.exports` | 导出函数列表 | |
| `report.attributes.signature_info.verified` | 数字签名总体状态 | 如 Signed / Unsigned |
| `report.attributes.signature_info.signers` | 签名者名称 | |
| `report.attributes.signature_info.signers details[*].status` | 每个签名者证书/签名状态 | |
| `report.attributes.signature_info.signers details[*].thumbprint` | 每个签名者证书指纹 | |
| `report.attributes.signature_info.counter signers details[*].status` | 每个副署名者证书/签名状态 | |
| `report.attributes.signature_info.counter signers details[*].thumbprint` | 每个副署名者证书指纹 | |
| `report.attributes.signature_info.x509[*].thumbprint` | x509 证书链指纹 | |
| `report.attributes.signature_info.x509[*].thumbprint_sha256` | x509 证书链 SHA256 指纹 | |
| `report.attributes.signature_info.x509[*].thumbprint_md5` | x509 证书链 MD5 指纹 | |
| `behaviour` | 各沙箱动态行为报告列表 | FILE 专用；无沙箱时为 null |
| `behaviour[i].attributes.registry_keys_set` | 写入/修改的注册表项 | 持久化、策略篡改线索 |
| `behaviour[i].attributes.services_created` | 创建的系统服务 | |
| `behaviour[i].attributes.services_started` | 启动的系统服务 | |
| `behaviour[i].attributes.command_executions` | 执行的 Shell 命令 | |
| `behaviour[i].attributes.processes_injected` | 被注入代码的进程 | 进程劫持线索 |
| `behaviour[i].attributes.windows_hidden` | 被隐藏的窗口 | 防御规避 |
| `behaviour[i].attributes.windows_searched` | 被搜索的窗口 | |
| `behaviour[i].attributes.mutexes_created` | 创建的互斥体 | 防多开/感染标记 |
| `behaviour[i].attributes.signals_observed` | 观察到的系统信号/广播 | |
| `behaviour[i].attributes.invokes` | 反射或运行时调用 | |
| `behaviour[i].attributes.files_written` | 写入的文件路径 | |
| `behaviour[i].attributes.files_deleted` | 删除的文件路径 | |
| `behaviour[i].attributes.files_opened` | 打开的文件路径 | |
| `behaviour[i].attributes.signals_hooked` | 挂钩/监听（键盘等） | 隐私窃取高风险 |
| `behaviour[i].attributes.calls_highlighted` | 重点 API 调用 | 如键盘、截图相关 |
| `behaviour[i].attributes.crypto_algorithms_observed` | 观察到的加密算法 | |
| `behaviour[i].attributes.crypto_keys` | 涉及的密钥材料 | |
| `mitre` | 各沙箱 MITRE ATT&CK 汇总 | Map，键为沙箱名 |
| `mitre.*.tactics` | 某沙箱下的战术列表 | `*` 如 CAPA、CAPE Sandbox |
| `mitre.*.tactics[*].id` | 战术 ID | 如 TA0007 |
| `mitre.*.tactics[*].name` | 战术名称 | 如 Discovery |
| `mitre.*.tactics[*].description` | 战术官方说明 | |
| `mitre.*.tactics[*].link` | 战术 MITRE 官网链接 | |
| `mitre.*.tactics[*].techniques` | 该战术下的技术列表 | List |
| `mitre.*.tactics[*].techniques[*].id` | 技术 ID | 如 T1082 |
| `mitre.*.tactics[*].techniques[*].name` | 技术名称 | |
| `mitre.*.tactics[*].techniques[*].description` | 技术官方说明 | |
| `mitre.*.tactics[*].techniques[*].signatures` | 触发该技术的沙箱证据 | 含 severity、match_data |

---

### 2.2 `url_skill.md`（`SkillEnum.URL`）

| 完整路径 | 含义 | 备注 |
| :--- | :--- | :--- |
| `report.id` | URL 在 VT 的 ID（SHA256） | Skill 写作 `id` |
| `report.type` | VT 对象类型字符串 | 值如 `url` |
| `url` | VirusTotal GUI 报告页链接 | 根节点字段 |
| `report.attributes.url` | 提交的原始 URL | 输出规范「目标 URL」 |
| `report.attributes.last_analysis_stats` | 各引擎检出数量汇总 | |
| `report.attributes.last_analysis_stats.malicious` | 判定为恶意的引擎数 | |
| `report.attributes.last_analysis_stats.suspicious` | 判定为可疑的引擎数 | |
| `report.attributes.last_analysis_stats.harmless` | 判定为无害的引擎数 | |
| `report.attributes.last_analysis_stats.undetected` | 未检出的引擎数 | |
| `report.attributes.last_analysis_results` | 各引擎详细结果 | Map |
| `report.attributes.last_analysis_results.*.engine_name` | 引擎名称 | |
| `report.attributes.last_analysis_results.*.method` | 检测方法 | |
| `report.attributes.last_analysis_results.*.result` | 检出名称/分类结果 | |
| `report.attributes.last_analysis_results.*.category` | 结果分类 | |
| `report.attributes.last_final_url` | 重定向后的最终 URL | 与原始 URL 差异大时需警惕钓鱼 |
| `report.attributes.redirection_chain` | 重定向跳转链 | List |
| `report.attributes.last_http_response_code` | 最后一次 HTTP 状态码 | |
| `report.attributes.last_http_response_headers` | 最后一次 HTTP 响应头 | Map |
| `report.attributes.last_http_response_content_length` | 响应正文长度（字节） | |
| `report.attributes.last_http_response_content_sha256` | 响应正文 SHA256 | |
| `report.attributes.last_http_response_cookies` | 响应 Set-Cookie 等 | Map |
| `report.attributes.html_meta` | 页面 Meta 标签 | 用于识别仿冒品牌 |
| `report.attributes.title` | 网页标题 | |
| `report.attributes.has_content` | 是否成功拉取到页面内容 | |
| `report.attributes.reputation` | 社区信誉分 | |
| `report.attributes.total_votes` | 社区投票（无害/恶意） | |
| `report.attributes.categories` | 厂商安全分类 | 如 phishing、malware |
| `report.attributes.tags` | 标签列表 | |
| `report.attributes.tld` | 顶级域名后缀 | |
| `report.attributes.targeted_brand` | 钓鱼目标品牌信息 | 非空为强恶意信号 |
| `report.attributes.favicon.raw_md5` | 网站图标 MD5 | 钓鱼套件或品牌仿冒关联 |
| `report.attributes.favicon.dhash` | 网站图标差分哈希 | |
| `report.attributes.trackers` | 页面追踪脚本历史 | Map |
| `report.attributes.trackers.*[*].id` | 追踪器标识 | `*` 为 tracker 家族 |
| `report.attributes.trackers.*[*].timestamp` | 追踪器采集时间 | |
| `report.attributes.trackers.*[*].url` | 追踪脚本 URL | |
| `report.attributes.outgoing_links` | 页内外链列表 | List |
| `report.attributes.first_submission_date` | 首次提交 VT 时间 | |
| `report.attributes.last_submission_date` | 最近提交 VT 时间 | |
| `report.attributes.last_analysis_date` | 最近扫描时间 | |
| `report.attributes.last_modification_date` | 对象最后修改时间 | |
| `report.attributes.times_submitted` | URL 累计提交次数 | 区分首次出现与反复上报基础设施 |

---

### 2.3 `ip_skill.md`（`SkillEnum.IP`）

| 完整路径 | 含义 | 备注 |
| :--- | :--- | :--- |
| `report.id` | IP 地址字符串 | Skill 写作 `id` |
| `report.type` | VT 对象类型字符串 | 值如 `ip_address` |
| `url` | VirusTotal GUI 报告页链接 | 根节点字段 |
| `report.attributes.last_analysis_stats` | 各引擎检出数量汇总 | |
| `report.attributes.last_analysis_stats.malicious` | 判定为恶意的引擎数 | |
| `report.attributes.last_analysis_stats.suspicious` | 判定为可疑的引擎数 | |
| `report.attributes.last_analysis_stats.harmless` | 判定为无害的引擎数 | |
| `report.attributes.last_analysis_stats.undetected` | 未检出的引擎数 | |
| `report.attributes.last_analysis_results` | 各引擎详细结果 | Map |
| `report.attributes.last_analysis_results.*.engine_name` | 引擎名称 | |
| `report.attributes.last_analysis_results.*.method` | 检测方法 | |
| `report.attributes.last_analysis_results.*.result` | 检出结果 | |
| `report.attributes.last_analysis_results.*.category` | 结果分类 | |
| `report.attributes.last_analysis_date` | 最近扫描时间 | 用于判断数据新鲜度 |
| `report.attributes.last_modification_date` | VT 对象最后修改时间 | |
| `report.attributes.asn` | 自治系统号（ASN） | |
| `report.attributes.as_owner` | AS 所属运营商/组织 | |
| `report.attributes.network` | 所属网段（CIDR） | |
| `report.attributes.continent` | 所在洲（ISO 代码） | |
| `report.attributes.country` | 所在国家/地区（ISO 代码） | |
| `report.attributes.regional_internet_registry` | 区域互联网注册机构 | 如 APNIC、RIPE |
| `report.attributes.last_https_certificate` | 最近关联的 HTTPS 证书对象 | null 时 Skill 要求跳过 |
| `report.attributes.last_https_certificate.subject.CN` | 证书主体主域名 | |
| `report.attributes.last_https_certificate.issuer` | 证书颁发者信息 | |
| `report.attributes.last_https_certificate.validity.not_before` | 证书生效时间 | |
| `report.attributes.last_https_certificate.validity.not_after` | 证书过期时间 | |
| `report.attributes.last_https_certificate.first_seen_date` | VT 首次观测该证书的时间 | |
| `report.attributes.last_https_certificate.thumbprint_sha256` | 证书 SHA256 指纹 | 基础设施复用关联 |
| `report.attributes.last_https_certificate.signature_algorithm` | 证书签名算法 | |
| `report.attributes.last_https_certificate.public_key.algorithm` | 公钥算法 | |
| `report.attributes.last_https_certificate.public_key.rsa.key_size` | RSA 公钥长度 | 公钥算法为 RSA 时存在 |
| `report.attributes.last_https_certificate.extensions.key_usage` | 证书密钥用途 | List |
| `report.attributes.last_https_certificate.extensions.extended_key_usage` | 证书扩展密钥用途 | List |
| `report.attributes.last_https_certificate.extensions.subject_alternative_name` | 证书 SAN 域名列表 | 可反映 IP 托管的多域名资产 |
| `report.attributes.last_https_certificate_date` | VT 获取该证书的时间 | |
| `report.attributes.jarm` | TLS JARM 指纹 | 可与 C2 框架特征关联 |
| `report.attributes.reputation` | 社区信誉分 | |
| `report.attributes.total_votes` | 社区投票 | |
| `report.attributes.tags` | 风险标签 | 如 c2、scanner、tor |
| `report.attributes.whois` | Whois 原始文本 | |
| `report.attributes.whois_date` | Whois 记录更新时间 | |

---

### 2.4 `domain_skill.md`（`SkillEnum.DOMAIN`）

| 完整路径 | 含义 | 备注 |
| :--- | :--- | :--- |
| `report.id` | 域名本身 | Skill 写作 `id` |
| `report.type` | VT 对象类型字符串 | 值如 `domain` |
| `url` | VirusTotal GUI 报告页链接 | 根节点字段 |
| `report.attributes.last_analysis_stats` | 各引擎检出数量汇总 | |
| `report.attributes.last_analysis_stats.malicious` | 判定为恶意的引擎数 | |
| `report.attributes.last_analysis_stats.suspicious` | 判定为可疑的引擎数 | |
| `report.attributes.last_analysis_stats.harmless` | 判定为无害的引擎数 | |
| `report.attributes.last_analysis_stats.undetected` | 未检出的引擎数 | |
| `report.attributes.last_analysis_results` | 各引擎详细结果 | Map |
| `report.attributes.last_analysis_results.*.engine_name` | 引擎名称 | |
| `report.attributes.last_analysis_results.*.method` | 检测方法 | |
| `report.attributes.last_analysis_results.*.result` | 检出结果 | |
| `report.attributes.last_analysis_results.*.category` | 结果分类 | |
| `report.attributes.last_analysis_date` | 最近扫描时间 | 用于判断数据新鲜度 |
| `report.attributes.last_modification_date` | VT 对象最后修改时间 | |
| `report.attributes.categories` | 厂商安全分类 | 如 phishing、c2 |
| `report.attributes.tags` | 标签列表 | 如 dga、malicious |
| `report.attributes.tld` | 顶级域名后缀 | |
| `report.attributes.creation_date` | 域名注册时间 | |
| `report.attributes.expiration_date` | 域名过期时间 | |
| `report.attributes.last_update_date` | Whois 最后更新时间 | |
| `report.attributes.registrar` | 注册商 | |
| `report.attributes.whois` | Whois 原始文本 | |
| `report.attributes.whois_date` | VT 更新 Whois 的时间 | |
| `report.attributes.last_dns_records` | 最近一次 DNS 解析记录 | List |
| `report.attributes.last_dns_records[*].type` | 记录类型 | 如 A、MX、CNAME |
| `report.attributes.last_dns_records[*].value` | 记录值 | 如 IP、邮件服务器 |
| `report.attributes.last_dns_records[*].ttl` | 缓存 TTL（秒） | 极短可能为 Fast Flux |
| `report.attributes.last_dns_records[*].priority` | MX/SRV 优先级 | |
| `report.attributes.last_dns_records[*].flag` | CAA 标志位 | |
| `report.attributes.last_dns_records[*].tag` | CAA tag | 如 issue、issuewild |
| `report.attributes.last_dns_records[*].serial` | SOA 序列号 | |
| `report.attributes.last_dns_records[*].refresh` | SOA refresh | |
| `report.attributes.last_dns_records[*].retry` | SOA retry | |
| `report.attributes.last_dns_records[*].expire` | SOA expire | |
| `report.attributes.last_dns_records[*].minimum` | SOA minimum TTL | |
| `report.attributes.last_dns_records[*].rname` | SOA 负责人邮箱式字段 | |
| `report.attributes.last_dns_records_date` | DNS 记录采集时间 | |
| `report.attributes.last_https_certificate` | 最近关联的 HTTPS 证书 | |
| `report.attributes.last_https_certificate.subject.CN` | 证书主体主域名 | |
| `report.attributes.last_https_certificate.issuer` | 证书颁发者 | |
| `report.attributes.last_https_certificate.validity.not_before` | 证书生效时间 | |
| `report.attributes.last_https_certificate.validity.not_after` | 证书过期时间 | |
| `report.attributes.last_https_certificate.first_seen_date` | VT 首次观测该证书的时间 | |
| `report.attributes.last_https_certificate.thumbprint_sha256` | 证书 SHA256 指纹 | 基础设施复用关联 |
| `report.attributes.last_https_certificate.signature_algorithm` | 证书签名算法 | |
| `report.attributes.last_https_certificate.public_key.algorithm` | 公钥算法 | |
| `report.attributes.last_https_certificate.public_key.rsa.key_size` | RSA 公钥长度 | 公钥算法为 RSA 时存在 |
| `report.attributes.last_https_certificate.extensions.key_usage` | 证书密钥用途 | List |
| `report.attributes.last_https_certificate.extensions.extended_key_usage` | 证书扩展密钥用途 | List |
| `report.attributes.last_https_certificate.extensions.subject_alternative_name` | 证书 SAN 域名列表 | |
| `report.attributes.last_https_certificate_date` | VT 获取证书的时间 | |
| `report.attributes.jarm` | TLS JARM 指纹 | |
| `report.attributes.popularity_ranks` | 流行度排名 | 如 Alexa；无排名+恶意特征风险更高 |
| `report.attributes.reputation` | 社区信誉分 | |
| `report.attributes.total_votes` | 社区投票 | |
| `report.attributes.crowdsourced_context` | 众包安全研究上下文 | List |
| `report.attributes.crowdsourced_context[*].title` | 众包上下文标题 | |
| `report.attributes.crowdsourced_context[*].severity` | 众包上下文严重性 | |
| `report.attributes.crowdsourced_context[*].details` | 众包上下文详情 | |
| `report.attributes.crowdsourced_context[*].source` | 众包上下文来源 | |
| `report.attributes.crowdsourced_context[*].timestamp` | 众包上下文时间戳 | |
| `report.attributes.favicon.raw_md5` | 网站图标 MD5 | 钓鱼仿冒比对 |
| `report.attributes.favicon.dhash` | 网站图标差分哈希 | |

---

## 3. 建模与 Skill 差异备忘

| 项 | 说明 |
| :--- | :--- |
| `ReportContent.type` | 枚举名 `FILE`/`URL`/…，与 `report.type`（VT 对象类型字符串）不同 |
| 签名状态/指纹的旧顶层写法 | 已移除；当前 Skill 使用 `signers details[]`、`counter signers details[]` 和 `x509[]` 下的真实 DTO 字段 |
| `behaviour` / `mitre` | 仅 FILE 有值；URL/IP/DOMAIN 为 `null` |
| `mitre` 结构 | `Map<沙箱名, { tactics: [...] }>`，非 VT 原始 API 的 `{ data: { ... } }` 包装（`VtResult.data` 已剥离） |
| 运行时 Skill 文件 | `SkillEnum` 加载 `skills/file_skill.md` 等英文版；`*_cn.md` 未接入枚举 |
