package com.progweb.siri_cascudo_api.exception;

import lombok.Getter;

@Getter
public enum ExceptionFlags {
    PRIMARY("primary"),
    SECONDARY("secondary"),
    SUCCESS("success"),
    DANGER("danger"),
    WARNING("warning"),
    INFO("info"),
    LIGHT("light"),
    DARK("dark");

    private final String value;

    ExceptionFlags(String value) {
        this.value = value;
    }

}
