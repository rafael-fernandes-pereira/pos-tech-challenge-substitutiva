package com.github.rafaelfernandes.login.adapter.out.persistence;

import com.github.rafaelfernandes.common.annotations.PersistenceAdapter;
import com.github.rafaelfernandes.login.application.domain.model.LoginHash;
import com.github.rafaelfernandes.login.application.port.out.LoginPort;
import com.github.rafaelfernandes.user.adapter.out.persistence.UserRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@AllArgsConstructor
public class LoginPersistenceAdapter implements LoginPort {

    private final UserRepository userRepository;

    @Override
    public Optional<LoginHash> findByCellphone(String cellphone) {

        return userRepository.findByCellphone(cellphone)
                .map(user -> new LoginHash(
                        user.getId().toString(),
                        user.getType().name(),
                        user.getCellphone(),
                        user.getPassword()));


    }
}
