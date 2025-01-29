package com.github.rafaelfernandes.login.adapter.out.persistence;

import com.github.rafaelfernandes.login.application.domain.model.Login;
import com.github.rafaelfernandes.user.adapter.out.persistence.UserEntity;

public class LoginMapper {

    public static Login toEntity(UserEntity userEntity) {
        return new Login(userEntity.getCellphone(), userEntity.getPassword());
    }

}
