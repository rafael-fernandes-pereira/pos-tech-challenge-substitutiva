package com.github.rafaelfernandes.user.application.domain.model;

import com.github.rafaelfernandes.user.common.enums.UserType;
import com.github.rafaelfernandes.user.common.utils.PasswordUtils;
import com.github.rafaelfernandes.user.common.validation.ValidPassword;
import com.github.rafaelfernandes.user.common.validation.Validation;
import com.github.rafaelfernandes.user.common.validation.ValueOfEnum;
import com.github.rafaelfernandes.user.common.validation.ValidationContactNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.UUID;

@Getter
public class User {

    private final UserId userId;

    @NotNull(message = "Telefone deve ser preenchido")
    @Size(min = 17, max = 17, message = "Telefone deve conter {min} caracteres")
    @ValidationContactNumber(message = "Telefone inválido. O telefone deve seguir o padrão +XX XX XXXXX-XXXX")
    private final String cellphone;

    @NotEmpty(message = "Tipo de usuário deve ser preenchido")
    @ValueOfEnum(enumClass = UserType.class, message = "Tipo de usuário inválido")
    private final String userType;

    @NotEmpty(message = "Senha deve ser preenchido")
    @ValidPassword
    private final String password;


    public record UserId(String id) {
        public UserId(String id) {
            this.id = id;
            Validation.validate(this);
        }
    }

    public User(String cellphone, String userType) {

        this.cellphone = cellphone;
        this.userType = userType;

        this.password = PasswordUtils.generatePassayPassword();
        Validation.validate(this);

        var id = UUID.randomUUID();

        this.userId = new UserId(id.toString());

    }

    public User(UserId userId, String cellphone, String userType, String password) {
        this.userId = userId;
        this.cellphone = cellphone;
        this.userType = userType;
        this.password = password;

        Validation.validate(this);
    }

    public static User of(UserId userId, String cellphone, String userType, String password) {
        return new User(userId, cellphone, userType, password);
    }

}
