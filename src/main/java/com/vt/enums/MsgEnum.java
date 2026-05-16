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

    // SSE Flow Status
    SSE_SYSTEM_INIT("sse.system.init"),
    SSE_SYSTEM_WAIT("sse.system.wait"),
    SSE_FLOW_START("sse.flow.start"),
    SSE_SCAN_SUBMIT("sse.scan.submit"),
    SSE_ANALYSE_START("sse.analyse.start"),
    SSE_ANALYSE_POLLING("sse.analyse.polling"),
    SSE_ANALYSE_CACHED("sse.analyse.cached"),
    SSE_REPORT_FETCH("sse.report.fetch"),
    SSE_REPORT_ERROR("sse.report.error"),
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
    PROMPT_SUMMARY_DATA_TITLE("prompt.summary.data.title");

    private final String key;

}
