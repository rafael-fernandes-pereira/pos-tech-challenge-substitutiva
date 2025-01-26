package com.github.rafaelfernandes.user.adapter.out.persistence;

import com.github.rafaelfernandes.user.application.domain.model.User;
import com.github.rafaelfernandes.user.application.port.out.UserPort;
import com.github.rafaelfernandes.common.annotations.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        var userEntity = UserMapper.toCreateEntity(user);

        var savedUserEntity = userRepository.save(userEntity);

        return UserMapper.toDomain(savedUserEntity);

    }
}
