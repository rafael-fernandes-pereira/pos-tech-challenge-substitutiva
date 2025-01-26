package com.github.rafaelfernandes.user.adapter.out.persistence;

import com.github.rafaelfernandes.user.application.domain.model.User;
import com.github.rafaelfernandes.user.common.enums.UserType;

import java.util.UUID;

public class UserMapper {

    public static UserEntity toCreateEntity(User user) {

        var userType = UserType.valueOf(user.getUserType());

        return UserEntity.builder()
                .residentId(UUID.fromString(user.getResident().get().getResidentId().id()))
                .type(userType)
                .password(user.getPassword())
                .build();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.of(
                new User.UserId(userEntity.getId().toString()),
                userEntity.getType().name(),
                userEntity.getPassword()

        );
    }

}
