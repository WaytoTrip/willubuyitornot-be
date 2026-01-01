package com.willu.buyitornot.service;

import com.willu.buyitornot.exception.ResourceNotFoundException;
import com.willu.buyitornot.infra.collection.User;
import com.willu.buyitornot.infra.repository.UserRepository;
import com.willu.buyitornot.web.ui.response.LoginResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    SwipeService swipeService;
    UserSwipeService userSwipeService;

    public User getUserById(ObjectId id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toHexString()));
    }

    @Transactional
    public LoginResponse login(String nickname) {
        return userRepository.findByNickname(nickname)
                .map(this::buildLoginResponseForExistingUser)
                .orElseGet(() -> buildLoginResponseForNewUser(nickname));
    }

    private LoginResponse buildLoginResponseForExistingUser(User user) {
        ObjectId latestSwipeId = swipeService.getLatestSwipeId()
                .orElseThrow(() -> new ResourceNotFoundException("Swipe", "latest", "none"));

        boolean alreadyParticipated = userSwipeService.hasUserParticipated(user.getId(), latestSwipeId);

        return LoginResponse.builder()
                .userId(user.getId().toHexString())
                .nickname(user.getNickname())
                .alreadyParticipated(alreadyParticipated)
                .build();
    }

    private LoginResponse buildLoginResponseForNewUser(String nickname) {
        User newUser = userRepository.save(new User(nickname));

        return LoginResponse.builder()
                .userId(newUser.getId().toHexString())
                .nickname(newUser.getNickname())
                .alreadyParticipated(false)
                .build();
    }
}
