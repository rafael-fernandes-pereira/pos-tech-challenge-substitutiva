package com.github.rafaelfernandes.login.application.port.in;

import com.github.rafaelfernandes.common.enums.UserType;

import java.util.UUID;

public interface TokenUseCase {

    void init();

    String generateToken(String cellphone, UserType userType, UUID id);

    Boolean isInvalid(String token);

}
