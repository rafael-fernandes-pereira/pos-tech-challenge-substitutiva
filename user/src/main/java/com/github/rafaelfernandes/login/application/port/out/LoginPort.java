package com.github.rafaelfernandes.login.application.port.out;

import com.github.rafaelfernandes.login.application.domain.model.Login;
import com.github.rafaelfernandes.login.application.domain.model.LoginHash;
import com.github.rafaelfernandes.user.application.domain.model.User;

import java.util.Optional;

public interface LoginPort {

    Optional<LoginHash> findByCellphone(String cellphone);

}
