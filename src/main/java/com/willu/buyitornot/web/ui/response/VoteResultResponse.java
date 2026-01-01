package com.willu.buyitornot.web.ui.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoteResultResponse {

    int totalGames;
    List<GenreStatDto> genreStats;
    List<SwipeTypeStatDto> swipeTypeStats;
    List<GameResultDto> games;
}