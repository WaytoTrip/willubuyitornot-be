package com.willu.buyitornot.web.ui.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetGamesResponse {

    String swipeId;
    int total;
    List<GameDto> games;
}