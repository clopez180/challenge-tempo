package com.challenge.tenpo.service;

import com.challenge.tenpo.model.UserData;

public interface ISessionSecurityService {

    boolean existSessionByToken(String token);

    UserData getUserData(String username);
}
