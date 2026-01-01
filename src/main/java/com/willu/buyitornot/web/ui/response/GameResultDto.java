package com.willu.buyitornot.web.ui.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameResultDto {

    String gameId;
    String imageUrl;
    String genre;
    Integer releaseYear;
    String title;
    String swipeType;
}