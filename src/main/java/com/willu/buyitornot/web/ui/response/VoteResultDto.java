package com.willu.buyitornot.web.ui.response;

import com.willu.buyitornot.domain.Decision;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoteResultDto {

    String gameId;
    Decision decision;
}