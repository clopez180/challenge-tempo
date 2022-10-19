package com.challenge.tenpo.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TokenResponse {
    private String key;

    private String value;
}
