package com.willu.buyitornot.service;

import com.willu.buyitornot.exception.ApiException;
import com.willu.buyitornot.exception.ErrorCode;
import com.willu.buyitornot.exception.ResourceNotFoundException;
import com.willu.buyitornot.infra.collection.Swipe;
import com.willu.buyitornot.infra.collection.UserSwipe;
import com.willu.buyitornot.infra.repository.SwipeRepository;
import com.willu.buyitornot.infra.repository.UserSwipeRepository;
import com.willu.buyitornot.web.ui.request.VoteDto;
import com.willu.buyitornot.web.ui.request.VoteRequest;
import com.willu.buyitornot.web.ui.response.VoteResponse;
import com.willu.buyitornot.web.ui.response.VoteResultDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSwipeService {

    UserSwipeRepository userSwipeRepository;
    SwipeRepository swipeRepository;

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

        LocalDateTime now = LocalDateTime.now();

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
}