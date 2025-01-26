package com.github.rafaelfernandes.user.application.domain.model;

import com.github.rafaelfernandes.user.common.enums.UserType;
import com.github.rafaelfernandes.user.common.utils.PasswordUtils;
import com.github.rafaelfernandes.user.common.validation.ValidPassword;
import com.github.rafaelfernandes.user.common.validation.Validation;
import com.github.rafaelfernandes.user.common.validation.ValueOfEnum;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

@Getter
public class User {

    private final UserId userId;

    @NotEmpty(message = "Tipo de usuário deve ser preenchido")
    @ValueOfEnum(enumClass = UserType.class, message = "Tipo de usuário inválido")
    private final String userType;

    @NotEmpty(message = "Senha deve ser preenchido")
    @ValidPassword
    private final String password;

    private Optional<Resident> resident;

    public record UserId(String id) {
        public UserId(String id) {
            this.id = id;
            Validation.validate(this);
        }
    }

    public User(Resident resident) {

        this.userType = UserType.RESIDENT.name();
        this.resident = Optional.of(resident);

        this.password = PasswordUtils.generatePassayPassword();
        Validation.validate(this);

        var id = UUID.randomUUID();

        this.userId = new UserId(id.toString());

    }

    private User(UserId userId, String userType, String password) {
        this.userId = userId;
        this.userType = userType;
        this.password = password;

        Validation.validate(this);
    }

    public static User of(UserId userId, String userType, String password) {
        return new User(userId, userType, password);
    }
}
