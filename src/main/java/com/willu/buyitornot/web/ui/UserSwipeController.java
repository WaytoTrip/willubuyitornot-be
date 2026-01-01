package com.willu.buyitornot.web.ui;

import com.willu.buyitornot.infra.collection.User;
import com.willu.buyitornot.service.UserSwipeService;
import com.willu.buyitornot.web.auth.AuthUser;
import com.willu.buyitornot.web.ui.common.WrapResponse;
import com.willu.buyitornot.web.ui.request.VoteRequest;
import com.willu.buyitornot.web.ui.response.VoteResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WrapResponse
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSwipeController {

    UserSwipeService userSwipeService;

    @PostMapping("/swipes/{swipeId}/votes")
    public VoteResponse saveVotes(@AuthUser User user,
                                  @PathVariable String swipeId,
                                  @Valid @RequestBody VoteRequest request) {
        return userSwipeService.saveVotes(user.getId(), swipeId, request);
    }
}
