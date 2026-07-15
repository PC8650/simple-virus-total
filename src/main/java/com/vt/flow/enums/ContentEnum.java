package com.vt.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentEnum {

    ERROR(false, false),
    NOTICE(false, false),
    PROMPT(true, true),
    THOUGHT(true, true),
    MAIN_TEXT(false, true);

    private final boolean fold;
    private final boolean copy;

}