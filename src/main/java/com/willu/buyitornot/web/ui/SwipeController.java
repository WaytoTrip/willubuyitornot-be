package com.willu.buyitornot.web.ui;

import com.willu.buyitornot.web.ui.common.WrapResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WrapResponse
@RestController
@RequestMapping("/swipe")
public class SwipeController {

    @GetMapping("/test")
    public String test() {
        return "Swipe Test Successful";
    }
}
