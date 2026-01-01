package com.willu.buyitornot.service;

import com.willu.buyitornot.infra.collection.Swipe;
import com.willu.buyitornot.infra.repository.SwipeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SwipeService {

    SwipeRepository swipeRepository;

    public Optional<ObjectId> getLatestSwipeId() {
        return swipeRepository.findTopByOrderByCreatedAtDesc()
                .map(Swipe::getId);
    }
}