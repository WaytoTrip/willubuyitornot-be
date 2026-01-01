package com.willu.buyitornot.web.ui;

import com.willu.buyitornot.service.GameService;
import com.willu.buyitornot.web.ui.common.WrapResponse;
import com.willu.buyitornot.web.ui.response.GetGamesResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@WrapResponse
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameController {

    GameService gameService;

    @GetMapping("/games")
    public GetGamesResponse getGames(@RequestHeader("x-user-id") String userId) { // TODO: 인증 추가
        return gameService.getGames();
    }
}
