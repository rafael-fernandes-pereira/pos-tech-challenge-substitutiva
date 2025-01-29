package com.github.rafaelfernandes.login.application.domain.model;

import com.github.rafaelfernandes.common.enums.UserType;
import com.github.rafaelfernandes.common.validation.Validation;
import com.github.rafaelfernandes.common.validation.ValidationContactNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.UUID;


@Getter
public class LoginHash {

    private final UUID userId;

    private final UserType userType;

    private final String cellphone;

    private final String passwordHash;

    public LoginHash(String userId, String userType, String cellphone, String passwordHash) {
        this.userId = UUID.fromString(userId);
        this.userType = UserType.valueOf(userType);
        this.cellphone = cellphone;
        this.passwordHash = passwordHash;

    }

}
