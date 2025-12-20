package com.willu.buyitornot.web.ui;

import com.willu.buyitornot.infra.collection.User;
import com.willu.buyitornot.service.UserService;
import com.willu.buyitornot.web.ui.common.WrapResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WrapResponse
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUserById(new ObjectId(id));
    }
}
