package com.willu.buyitornot.web.ui;

import com.willu.buyitornot.service.UserService;
import com.willu.buyitornot.web.ui.common.WrapResponse;
import com.willu.buyitornot.web.ui.request.LoginRequest;
import com.willu.buyitornot.web.ui.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@WrapResponse
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginController {

    UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request.getNickname());
    }
}