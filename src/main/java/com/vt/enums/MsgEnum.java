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
    SSE_SUMMARY_START("sse.summary.start"),
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
    ENHANCEMENT_REFERENCE_TITLE("enhancement.reference.title");

    private final String key;

}
