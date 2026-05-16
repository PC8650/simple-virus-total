# Simple VirusTotal AI Threat Analysis Platform

## 1. 项目简介与目的

**VirusTotal** (访问地址: [https://www.virustotal.com/](https://www.virustotal.com/)) 是一个免费且强大的安全情报服务平台，能够通过多款反病毒引擎和安全扫描工具对可疑文件、域名、IP 和 URL 进行深度检测。然而，VirusTotal 返回的原始数据极其庞杂（包含大量的 JSON 字段、专业术语如 ATT&CK 战术、JARM 指纹等），对非专业安全人员来说门槛较高。

**项目初衷**：通过对接 VirusTotal 接口获取原始分析数据，并接入 **Google GenAI 大模型**，将这些晦涩难懂的专业数据转化为通俗易懂的“安全科普式报告”。用户不仅能直接看到“有害/可疑/安全”的定性结论，还能通过大模型的“战术卡片”通俗地理解攻击原理和潜在危害。使普通用户能够更轻松的 **参考** VirusTotal 分析结果。

---

## 2. API 对接与局限性声明

本项目对接了 VirusTotal 的官方 API，但存在以下局限性：
- **基于免费 API (Public API)**：项目完全基于 VT 免费接口开发，受到并发和调用频率的严格限制。
- **功能覆盖有限**：目前仅对接了基础的**公开扫描 (Public Scan)**和**基础报告获取 (Basic Report)**接口，涵盖对象包括 File (文件)、URL、Domain (域名) 和 IP 地址。
- **参数折损**：由于免费版接口权限和复杂数据结构的清洗原因，在从 VT 原始响应向项目内部 DTO 转换的过程中，可能会丢失部分非核心的高级参数和深度动态行为追踪数据。

---

## 3. 开发环境与项目结构

### 核心环境
- **Java 版本**：JDK 25

### 目录结构简析
```text
simple-virus-total
├── src/main/java/com/vt/
│   ├── remote/        # 【外部服务交互层】专门负责与 VirusTotal API 的通信
│   │   ├── api/       # API 路由定义
│   │   ├── component/ # 发送 HTTP 请求的具体实现组件
│   │   ├── controller/# 本地暴露的代理接口
│   │   └── dto/       # 与 VT 交互的数据传输对象
│   ├── flow/          # 【AI 业务流层】负责将 VT 数据组装为 Prompt，调用 AI 并通过 SSE 流式推流
│   │   ├── advisor/   # 负责与大模型进行交互，构建 Prompt 和拦截流数据
│   │   ├── component/ # 业务流转组件核心实现
│   │   ├── controller/# 处理前端发起的 SSE 连接与交互请求
│   │   ├── dto/       # 业务层数据传输对象
│   │   ├── enums/     # 业务流相关枚举定义（含 SkillEnum 等）
│   │   ├── scan/      # 负责调用 Remote 层发起 VT 扫描并轮询结果
│   │   ├── utils/     # 业务内部专门的工具类（如 SSE 发送工具等）
│   │   └── vo/        # 返回给前端的视图对象
│   ├── config/        # 全局配置类
│   ├── enums/         # 错误码及国际化枚举
│   ├── exception/     # 包装异常类
│   └── utils/         # 工具类（包含国际化、SSE 推送等）
├── src/main/resources/
│   ├── skills/        # 【核心 AI 提示词】包含各个分析维度的专家规则手册
│   ├── static/        # 前端 UI 资源（HTML / JS / CSS）
│   ├── i18n/          # 国际化语言包
│   └── application.yml# Spring Boot 核心配置文件。
```

---

## 4. 核心配置参数说明 (`application.yml`)

项目的所有核心动态配置均集中在配置文件的 `params` 节点下：

| 参数路径 | 默认值 / 示例 | 说明 |
| :--- | :--- | :--- |
| `params.port` | `8080` | Web 服务的启动端口。 |
| `params.v-key` | 无 (必填) | **VirusTotal API Key**，用于请求扫描报告。 |
| `params.google.key` | 无 (必填) | **Google GenAI API Key**，用于驱动 AI 总结功能。 |
| `params.google.model` | `gemma-4-31b-it` | 使用的 AI 模型版本。 |
| `params.google.search` | `false` | 是否允许模型使用 Google Search 进行联网信息增强。 |
| `params.google.thinking-level`| `high` | 模型的推理深度等级 (可选 `minimal` / `low` / `medium` / `high`)。 |

---

## 5. 打包说明与外部参数配置

本项目推荐使用 `jpackage` 打包为跨平台的“绿色版”应用程序映像 (app-image)。详细的打包参数与不同系统（Windows/macOS/Linux）的具体打包指引，请参阅项目根目录或 resources 下的 [package_command.md](src/main/resources/package_command.md)。

**外部配置指引 (推荐)**：
在软件打包分发后，为避免将 API Key 泄露或硬编码在程序内部，建议使用 Spring 的外部配置覆盖能力：
1. 在生成的可执行文件（如 `simple-vt.exe`）同级目录下，手动创建一个 `config` 文件夹。
2. 在其中创建 `application.yml`。
3. 填入上述第 4 节中的 `params` 参数进行覆盖。程序启动时会自动优先加载此外部配置文件。

---

## 6. 使用说明

1. 确保配置了有效的 VirusTotal API Key 和 Google GenAI Key。
2. 启动应用程序（可通过 IDE 直接启动 `VirusTotalApplication`，或运行打包后的可执行文件）。
3. 打开浏览器，访问前端交互界面：
   👉 **http://localhost:8080/index.html** (将 `localhost` 替换为你的服务器 IP，将 `8080` 替换为你配置的端口)
4. 在页面中上传文件或输入 URL/IP/域名，点击分析即可体验可视化的 AI 威胁分析实况推流。
