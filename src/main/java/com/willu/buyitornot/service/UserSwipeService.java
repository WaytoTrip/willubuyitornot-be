package com.willu.buyitornot.service;

import com.willu.buyitornot.infra.repository.UserSwipeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSwipeService {

    UserSwipeRepository userSwipeRepository;

    public boolean hasUserParticipated(ObjectId userId, ObjectId swipeId) {
        return userSwipeRepository.existsByUserIdAndSwipeId(userId, swipeId);
    }
}