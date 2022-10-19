package com.challenge.tenpo.unit.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.mock;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthenticationMocks {

  public static Authentication getDefault() {
    return mock(Authentication.class);
  }
}
