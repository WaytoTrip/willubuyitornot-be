package com.willu.buyitornot.web.ui.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 1, max = 20, message = "닉네임은 1~20자 사이여야 합니다.")
    String nickname;
}