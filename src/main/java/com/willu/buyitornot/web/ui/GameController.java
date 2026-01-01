package com.willu.buyitornot.web.ui;

import com.willu.buyitornot.infra.collection.User;
import com.willu.buyitornot.service.GameService;
import com.willu.buyitornot.web.auth.AuthUser;
import com.willu.buyitornot.web.ui.common.WrapResponse;
import com.willu.buyitornot.web.ui.response.GetGamesResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@WrapResponse
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameController {

    GameService gameService;

    @GetMapping("/games")
    public GetGamesResponse getGames(@AuthUser User user) {
        return gameService.getGames();
    }
}
