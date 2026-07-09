package com.vt.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentEnum {

    ERROR(false),
    NOTICE(false),
    PROMPT(true),
    THOUGHT(true),
    MAIN_TEXT(false);

    private final boolean fold;

}
