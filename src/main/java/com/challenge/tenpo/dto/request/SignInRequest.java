package com.challenge.tenpo.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
public class SignInRequest extends LoginRequest {

    private String confirmPassword;
}
