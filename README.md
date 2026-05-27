# Simple VirusTotal AI Threat Analysis Platform

## 1. 项目简介与目的

**VirusTotal** (访问地址: [https://www.virustotal.com/](https://www.virustotal.com/)) 是一个免费且强大的安全情报服务平台，能够通过多款反病毒引擎和安全扫描工具对可疑文件、域名、IP 和 URL 进行深度检测。然而，VirusTotal 返回的原始数据极其庞杂（包含大量的 JSON 字段、专业术语如 ATT&CK 战术、JARM 指纹等），对非专业安全人员来说门槛较高。

**项目初衷**：通过对接 VirusTotal 接口获取原始分析数据，并接入大模型(默认 Google GenAI)，将这些晦涩难懂的专业数据转化为通俗易懂的“分析报告”。用户不仅能直接看到“有害/可疑/安全”的定性结论，还能通过大模型的“战术卡片”通俗地理解攻击原理和潜在危害。使普通用户能够更轻松的 **参考** VirusTotal 分析结果。

---

## 2. API 对接与局限性声明

本项目对接了 VirusTotal 的官方 API，但存在以下局限性：
- **基于免费 API (Public API)**：项目完全基于 VT 免费接口开发，受到并发和调用频率的严格限制。
- **功能覆盖有限**：目前仅对接了基础的**公开扫描 (Public Scan)** 和 **基础报告获取 (Basic Report)** 接口，涵盖对象包括 File (文件)、URL、Domain (域名) 和 IP 地址。
- **参数折损**：由于免费版接口权限和复杂数据结构的清洗原因，在从 VT 原始响应向项目内部 DTO 转换的过程中，可能会丢失部分非核心的高级参数和深度动态行为追踪数据。

---

## 3. 开发环境与项目结构

### 核心环境
- **Java 版本**：JDK 25

### 目录结构简析
```text
simple-virus-total
├── src/
│   └── main/
│       ├── java/com/vt/
│       │   ├── config/          # 全局基础配置（HTTP/Gson/i18n 等）
│       │   ├── enums/           # 国际化消息枚举
│       │   ├── exception/       # 异常封装
│       │   ├── utils/           # 通用工具类
│       │   ├── flow/            # AI 业务流（扫描、轮询、Prompt 编排、SSE 输出）
│       │   │   ├── advisor/     # Scan/Analyse/Report/Summary 顾问链
│       │   │   │   └── constant/# advisor 链路常量
│       │   │   ├── component/   # 流程组件（CacheManager/ExternalSkillManager）
│       │   │   ├── config/      # AI 与外部技能配置绑定
│       │   │   ├── controller/  # AI 业务入口控制层
│       │   │   ├── dto/         # 流程数据传输对象
│       │   │   ├── enums/       # 流程类型与技能枚举
│       │   │   ├── model/       # 通用 ChatModel 适配
│       │   │   ├── scan/        # 扫描器实现
│       │   │   │   ├── factory/ # 扫描器工厂
│       │   │   │   └── interfaces/ # 扫描器接口定义
│       │   │   ├── utils/       # SSE、轮询、Markdown/YAML 解析等工具
│       │   │   └── vo/          # 对前端输出结构
│       │   ├── remote/          # VirusTotal API 交互层
│       │   │   ├── api/         # VT 接口封装
│       │   │   │   ├── constant/# API 常量
│       │   │   │   └── enums/   # API 枚举
│       │   │   ├── component/   # VT 远程调用组件（VtRemoter）
│       │   │   ├── controller/  # 本地代理接口暴露层
│       │   │   └── dto/         # VT 请求/响应对象
│       │   │       └── vt/      # VT 细分对象（file/url/ip/domain 等）
│       │   └── VirusTotalApplication.java
│       └── resources/
│           ├── application.yml  # Spring 配置入口（支持 params.* 外部覆盖）
│           ├── i18n/            # 中英文消息包
│           ├── skills/          # 内置分析 skill（file/url/ip/domain，中英文）
│           └── static/          # Web 前端静态资源
├── external_skill_guide.md      # 外部 skill 目录规范与使用说明
├── package_command.md           # jpackage 打包与外部配置覆盖说明
└── pom.xml
```

---

## 4. 核心配置参数说明 (`application.yml`)

项目运行参数通过 `params.*` 覆盖 `application.yml` 中的占位符，推荐在外部 `config/application.yml` 中配置：

| 参数路径 | 默认值 / 示例 | 说明 |
| :--- | :--- | :--- |
| `params.port` | `8080` | Web 服务的启动端口。 |
| `params.v-key` | 无 (必填) | **VirusTotal API Key**，用于请求扫描报告。 |
| `params.google.key` | 无 (必填) | **Google GenAI API Key**，用于驱动 AI 总结功能。 |
| `params.google.model` | `gemma-4-31b-it` | 使用的 AI 模型版本。 |
| `params.google.search` | `false` | 是否允许模型使用 Google Search 进行联网信息增强。 |
| `params.google.thinking-level`| `high` | 模型的推理深度等级 (可选 `minimal` / `low` / `medium` / `high`)。 |
| `params.customer.ai` | `false` | 是否启用自定义 AI 厂商（启用后走 `ai-provider.*` 分支，不使用默认 Google 模型）。 |
| `params.customer.base-url` | 空 | 自定义 AI 接口地址（需兼容 OpenAI Chat Completions 协议）。 |
| `params.customer.api-key` | 空 | 自定义 AI 厂商 API Key。 |
| `params.customer.model` | 空 | 自定义 AI 模型名。 |
| `params.customer.temperature` | `0` | 自定义 AI 温度参数，安全分析建议维持低温度。 |
| `params.external.enable` | `false` | 是否启用外部 skill 增强。 |
| `params.external.dir` | 空 | 外部 skill 根目录绝对路径。 |
| `params.external.top-limit` | `3` | 单次最多注入的外部 skill 数量（必须 > 0 才生效）。 |

---

## 5. 外部 Skill 说明

### 功能定位
外部 skill 用于在 AI 生成分析报告前，按 VirusTotal 返回内容进行标签匹配并注入额外专家知识。该能力由 `ExternalSkillManager` 在启动时完成索引构建。

### 启用配置
在外部配置中设置以下参数：

```yaml
params:
  external:
    enable: true
    dir: "D:/my-security-skills"
    top-limit: 3
```

### 文件组织与最小格式
- 系统会递归扫描 `dir` 下所有 `.md` 文件。
- 仅识别带 YAML Frontmatter 且包含 `tags` 字段的文档。
- 若某个 skill 文件同级存在 `references/` 目录，会自动加载其中 `.md` 作为补充参考内容。

示例：
```markdown
---
description: "PE Overlay 检测"
tags:
  - overlay
  - peexe
  - T1027
---

## 分析步骤
...
```

### 匹配与排序机制
- 基于标签命中做等权计分：每命中 1 个 tag 记 1 分。
- 按得分降序选择前 `top-limit` 个 skill 注入提示词。
- `top-limit` 设置越大，提示词越长，可能导致模型对原始 VT 数据关注下降。

完整规范与最佳实践见 `external_skill_guide.md`。

---

## 6. 打包说明与外部参数配置

本项目推荐使用 `jpackage` 打包为跨平台的“绿色版”应用程序映像 (app-image)。详细的打包参数与不同系统（Windows/macOS/Linux）的具体打包指引，请参阅项目根目录下的 `package_command.md`。

**外部配置指引**：
在软件打包分发后，为避免将 API Key 泄露或硬编码在程序内部，建议使用 Spring 的外部配置覆盖能力：
1. 在生成的可执行文件（如 `simple-vt.exe`）同级目录下，手动创建一个 `config` 文件夹。
2. 在其中创建 `application.yml`。
3. 填入上述第 4 节中的 `params` 参数进行覆盖。程序启动时会自动优先加载此外部配置文件。

---

## 7. 使用说明

1. 确保配置了有效的 VirusTotal API Key 和 Google GenAI Key。
2. 启动应用程序（可通过 IDE 直接启动 `VirusTotalApplication`，或运行打包后的可执行文件）。
3. 打开浏览器，访问前端交互界面：
   👉 **http://localhost:8080/index.html** (将 `localhost` 替换为你的服务器 IP，将 `8080` 替换为你配置的端口)
4. 在页面中上传文件或输入 URL/IP/域名，点击分析即可体验可视化的 AI 威胁分析实况推流。
