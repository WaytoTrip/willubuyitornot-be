package com.willu.buyitornot.web.ui.request;

import com.willu.buyitornot.domain.Decision;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteDto {

    @NotNull(message = "gameId는 필수입니다.")
    String gameId;

    @NotNull(message = "decision은 필수입니다.")
    Decision decision;
}