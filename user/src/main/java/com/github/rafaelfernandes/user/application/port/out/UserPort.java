package com.github.rafaelfernandes.user.application.port.out;

import com.github.rafaelfernandes.user.application.domain.model.User;

public interface UserPort {

    User save(User user);
}
