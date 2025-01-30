package com.github.rafaelfernandes.user.application.port.out;

import com.github.rafaelfernandes.user.application.domain.model.User;

import java.util.Optional;

public interface UserPort {

    User save(User user);

    Optional<User> findByCellphone(String cellphone);
}
