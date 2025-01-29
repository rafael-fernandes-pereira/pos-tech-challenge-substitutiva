package com.github.rafaelfernandes.login.application.port.in;

import com.github.rafaelfernandes.login.application.domain.model.Login;

public interface AuthenticateUseCase {

    String authenticate(Login login);

    void isInvalid(String token);
}
