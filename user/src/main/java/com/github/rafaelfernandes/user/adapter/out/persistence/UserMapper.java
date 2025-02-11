package com.github.rafaelfernandes.user.adapter.out.persistence;

import com.github.rafaelfernandes.user.adapter.out.persistence.entity.UserEntity;
import com.github.rafaelfernandes.user.application.domain.model.User;
import com.github.rafaelfernandes.common.enums.UserType;

import java.util.UUID;

public class UserMapper {

    public static UserEntity toCreateEntity(User user) {

        var userType = UserType.valueOf(user.getUserType());

        return UserEntity.builder()
                .cellphone(user.getCellphone())
                .type(userType)
                .password(user.getPassword())
                .build();
    }

    public static UserEntity toEntity(User user) {

        var userType = UserType.valueOf(user.getUserType());

        return UserEntity.builder()
                .id(UUID.fromString(user.getUserId().id()))
                .cellphone(user.getCellphone())
                .type(userType)
                .password(user.getPassword())
                .build();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.of(
                userEntity.getId().toString(),
                userEntity.getType().name(),
                userEntity.getCellphone(),
                userEntity.getPassword()

        );
    }

}
