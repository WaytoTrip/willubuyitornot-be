package com.willu.buyitornot.service;

import com.willu.buyitornot.exception.ErrorCode;
import com.willu.buyitornot.exception.ResourceNotFoundException;
import com.willu.buyitornot.infra.collection.Game;
import com.willu.buyitornot.infra.collection.Swipe;
import com.willu.buyitornot.infra.repository.GameRepository;
import com.willu.buyitornot.infra.repository.SwipeRepository;
import com.willu.buyitornot.web.ui.response.GameDto;
import com.willu.buyitornot.web.ui.response.GetGamesResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameService {

    SwipeRepository swipeRepository;
    GameRepository gameRepository;

    public GetGamesResponse getGames() {
        Swipe latestSwipe = swipeRepository.findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.LATEST_SWIPE_NOT_FOUND));

        List<Game> games = gameRepository.findAllByIdIn(latestSwipe.getGameIdList());

        if (games.isEmpty()) {
            log.error("Game not found. swipeId: {}, gameIds: {}", latestSwipe.getId().toHexString(), latestSwipe.getGameIdList());
            throw new ResourceNotFoundException(ErrorCode.GAME_NOT_FOUND);
        }

        List<GameDto> gameDtos = games.stream()
                .map(game -> GameDto.builder()
                        .gameId(game.getId().toHexString())
                        .imageUrl(game.getImageUrl())
                        .genre(game.getGenre())
                        .releaseYear(game.getReleaseYear())
                        .title(game.getTitle())
                        .build())
                .toList();

        return GetGamesResponse.builder()
                .swipeId(latestSwipe.getId().toHexString())
                .total(gameDtos.size())
                .games(gameDtos)
                .build();
    }
}