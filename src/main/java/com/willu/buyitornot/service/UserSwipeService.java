package com.willu.buyitornot.service;

import com.willu.buyitornot.exception.ApiException;
import com.willu.buyitornot.exception.ErrorCode;
import com.willu.buyitornot.exception.ResourceNotFoundException;
import com.willu.buyitornot.infra.collection.Game;
import com.willu.buyitornot.infra.collection.Swipe;
import com.willu.buyitornot.infra.collection.UserSwipe;
import com.willu.buyitornot.infra.repository.GameRepository;
import com.willu.buyitornot.infra.repository.SwipeRepository;
import com.willu.buyitornot.infra.repository.UserSwipeRepository;
import com.willu.buyitornot.web.ui.request.VoteDto;
import com.willu.buyitornot.web.ui.request.VoteRequest;
import com.willu.buyitornot.web.ui.response.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSwipeService {

    UserSwipeRepository userSwipeRepository;
    SwipeRepository swipeRepository;
    GameRepository gameRepository;

    public boolean hasUserParticipated(ObjectId userId, ObjectId swipeId) {
        return userSwipeRepository.existsByUserIdAndSwipeId(userId, swipeId);
    }

    @Transactional
    public VoteResponse saveVotes(ObjectId userId, String swipeIdStr, VoteRequest request) {
        ObjectId swipeId = new ObjectId(swipeIdStr);
        Swipe swipe = swipeRepository.findById(swipeId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.SWIPE_NOT_FOUND, "swipeId: " + swipeIdStr));

        if (hasUserParticipated(userId, swipeId)) {
            throw new ApiException(ErrorCode.ALREADY_PARTICIPATED, "userId: %s, swipeId: %s".formatted(userId, swipeId));
        }

        Set<String> validGameIds = swipe.getGameIdList().stream()
                .map(ObjectId::toHexString)
                .collect(Collectors.toSet());

        List<ObjectId> buyList = new ArrayList<>();
        List<ObjectId> skipList = new ArrayList<>();
        List<ObjectId> maybeList = new ArrayList<>();

        for (VoteDto vote : request.getVotes()) {
            if (!validGameIds.contains(vote.getGameId())) {
                throw new ResourceNotFoundException(ErrorCode.INVALID_GAME_IN_SWIPE, "gameId: " + vote.getGameId());
            }

            ObjectId gameId = new ObjectId(vote.getGameId());
            switch (vote.getDecision()) {
                case BUY -> buyList.add(gameId);
                case SKIP -> skipList.add(gameId);
                case MAYBE -> maybeList.add(gameId);
            }
        }

        UserSwipe userSwipe = new UserSwipe(userId, swipeId);
        userSwipe.setBuy(buyList);
        userSwipe.setSkip(skipList);
        userSwipe.setMaybe(maybeList);

        userSwipeRepository.save(userSwipe);

        List<VoteResultDto> voteResults = request.getVotes().stream()
                .map(vote -> VoteResultDto.builder()
                        .gameId(vote.getGameId())
                        .decision(vote.getDecision())
                        .build())
                .toList();

        return VoteResponse.builder()
                .swipeId(swipeIdStr)
                .savedCount(voteResults.size())
                .votes(voteResults)
                .build();
    }

    @Transactional(readOnly = true)
    public VoteResultResponse getVoteResult(ObjectId userId, String swipeIdStr) {
        ObjectId swipeId = new ObjectId(swipeIdStr);

        swipeRepository.findById(swipeId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.SWIPE_NOT_FOUND, "swipeId: " + swipeIdStr));

        UserSwipe userSwipe = userSwipeRepository.findByUserIdAndSwipeId(userId, swipeId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_SWIPE_NOT_FOUND,
                        "userId: %s, swipeId: %s".formatted(userId.toHexString(), swipeIdStr)));

        List<ObjectId> allGameIds = new ArrayList<>();
        allGameIds.addAll(userSwipe.getBuy());
        allGameIds.addAll(userSwipe.getSkip());
        allGameIds.addAll(userSwipe.getMaybe());

        Map<ObjectId, Game> gameMap = gameRepository.findAllByIdIn(allGameIds).stream()
                .collect(Collectors.toMap(Game::getId, Function.identity()));

        Set<String> buySet = userSwipe.getBuy().stream().map(ObjectId::toHexString).collect(Collectors.toSet());
        Set<String> maybeSet = userSwipe.getMaybe().stream().map(ObjectId::toHexString).collect(Collectors.toSet());

        List<GameResultDto> games = new ArrayList<>();
        Map<String, Integer> genreCount = new HashMap<>();

        for (ObjectId gameId : allGameIds) {
            Game game = gameMap.get(gameId);
            if (game == null) continue;

            String gameIdStr = gameId.toHexString();
            String swipeType;
            if (buySet.contains(gameIdStr)) {
                swipeType = "BUY";
            } else if (maybeSet.contains(gameIdStr)) {
                swipeType = "MAYBE";
            } else {
                swipeType = "SKIP";
            }

            games.add(GameResultDto.builder()
                    .gameId(gameIdStr)
                    .imageUrl(game.getImageUrl())
                    .genre(game.getGenre())
                    .releaseYear(game.getReleaseYear())
                    .title(game.getTitle())
                    .swipeType(swipeType)
                    .build());

            genreCount.merge(game.getGenre(), 1, Integer::sum);
        }

        List<GenreStatDto> genreStats = genreCount.entrySet().stream()
                .map(e -> GenreStatDto.builder()
                        .name(e.getKey())
                        .count(e.getValue())
                        .build())
                .toList();

        List<SwipeTypeStatDto> swipeTypeStats = List.of(
                SwipeTypeStatDto.builder().type("BUY").count(userSwipe.getBuy().size()).build(),
                SwipeTypeStatDto.builder().type("MAYBE").count(userSwipe.getMaybe().size()).build(),
                SwipeTypeStatDto.builder().type("SKIP").count(userSwipe.getSkip().size()).build()
        );

        return VoteResultResponse.builder()
                .totalGames(games.size())
                .genreStats(genreStats)
                .swipeTypeStats(swipeTypeStats)
                .games(games)
                .build();
    }
}