package com.api.campuslink.utils;

import lombok.Getter;

@Getter
public enum QueryTypeEnum {
    GENERAL("GENERAL"),
    TECHNICAL("TECHNICAL"),
    BILLING("BILLING"),
    FEEDBACK("FEEDBACK"),
    COMPLAINT("COMPLAINT"),
    SUGGESTION("SUGGESTION");

    private final String value;

    QueryTypeEnum(String value) {
        this.value = value;
    }

    public static QueryTypeEnum fromValue(String value) {
        for (QueryTypeEnum type : QueryTypeEnum.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown query type: " + value);
    }
}
