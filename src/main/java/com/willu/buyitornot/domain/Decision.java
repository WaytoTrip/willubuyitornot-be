package com.willu.buyitornot.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Decision {
    BUY("buy"),
    SKIP("skip"),
    MAYBE("maybe");

    String value;

    Decision(String value) {
        this.value = value;
    }
}