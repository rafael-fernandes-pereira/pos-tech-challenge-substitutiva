package com.github.rafaelfernandes.user.adapter.out.persistence;

import com.github.rafaelfernandes.user.application.domain.model.User;
import com.github.rafaelfernandes.user.application.port.out.UserPort;
import com.github.rafaelfernandes.common.annotations.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        var userEntity = UserMapper.toCreateEntity(user);

        var encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        userEntity.setPassword(encryptedPassword);

        userRepository.save(userEntity);

        return user;

    }
}
