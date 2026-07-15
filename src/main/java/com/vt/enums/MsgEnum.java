package com.vt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgEnum {
    // Exceptions
    VT_ERROR_BUILD("vt.error.build"),
    VT_ERROR_CALL("vt.error.call"),
    VT_ERROR_PARSE("vt.error.parse"),

    // System Exceptions
    SYS_TIMEOUT_RESULT("sys.timeout.result"),
    SSE_EMITTER_ERROR("sse.emitter.error"),
    SYS_NO_SCANNER("sys.no.scanner"),
    SYS_FILE_HASH_ERROR("sys.file.hash.error"),
    SYS_AI_CONFIG_ERROR("sys.ai.config.error"),
    SYS_SKILL_LOAD_ERROR("sys.skill.load.error"),
    SYS_ANALYSE_ID_ERROR("sys.analyse.id.error"),

    // Validation Exceptions
    SCAN_ERR_URL_FORMAT("scan.err.url.format"),
    SCAN_ERR_IP_FORMAT("scan.err.ip.format"),
    SCAN_ERR_FILE_EMPTY("scan.err.file.empty"),
    SCAN_ERR_DOMAIN_FORMAT("scan.err.domain.format"),
    SCAN_ERR_FILE_SIZE("scan.err.file.size"),
    SCAN_ERR_API_FAILED("scan.err.api.failed"),

    // SSE Flow Status
    SSE_SCAN_SUBMIT("sse.scan.submit"),
    SSE_ANALYSE_START("sse.analyse.start"),
    SSE_ANALYSE_POLLING("sse.analyse.polling"),
    SSE_ANALYSE_CACHED("sse.analyse.cached"),
    SSE_REPORT_FETCH("sse.report.fetch"),
    SSE_REPORT_SUMMARY("sse.report.summary"),
    SSE_REPORT_SANDBOX_START("sse.report.sandbox.start"),
    SSE_REPORT_SANDBOX_POLLING("sse.report.sandbox.polling"),
    SSE_REPORT_MITRE_START("sse.report.mitre.start"),
    SSE_SUMMARY_PROMPT("sse.summary.prompt"),
    SSE_SUMMARY_PROMPT_AUDIT("sse.summary.prompt_audit"),
    SSE_SUMMARY_PROMPT_SYS("sse.summary.prompt_sys"),
    SSE_SUMMARY_PROMPT_USR("sse.summary.prompt_usr"),
    SSE_SUMMARY_START_ANALYSE("sse.summary.start_analyse"),
    SSE_TASK_ERROR("sse.task.error"),
    SSE_TASK_INTERNAL_ERROR("sse.task.internal_error"),
    SSE_TASK_FINISH("sse.task.finish"),

    // AI Prompts
    PROMPT_INITIAL("prompt.initial"),
    PROMPT_INITIAL_EMPTY("prompt.initial.empty"),
    PROMPT_SUMMARY_TASK("prompt.summary.task"),
    PROMPT_SUMMARY_BG_TITLE("prompt.summary.bg.title"),
    PROMPT_SUMMARY_DATA_TITLE("prompt.summary.data.title"),

    //  AI External Skill Enhancement
    ENHANCEMENT_HEADER("enhancement.header"),
    ENHANCEMENT_GUIDE_TITLE("enhancement.guide.title"),
    ENHANCEMENT_REFERENCE_TITLE("enhancement.reference.title"),

    // LLM API Exceptions
    LLM_ERR_EMPTY_RESPONSE("llm.err.empty.response"),
    LLM_ERR_SYNC_FAILED("llm.err.sync.failed"),
    LLM_ERR_STREAM_FAILED("llm.err.stream.failed"),
    LLM_ERR_PARSE_CHUNK_FAILED("llm.err.parse.chunk.failed");

    private final String key;

}