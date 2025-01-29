package com.github.rafaelfernandes.login.application.domain.model;

import com.github.rafaelfernandes.common.validation.ValidPassword;
import com.github.rafaelfernandes.common.validation.Validation;
import com.github.rafaelfernandes.common.validation.ValidationContactNumber;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class Login {

    @NotNull(message = "Telefone deve ser preenchido")
    @Size(min = 17, max = 17, message = "Telefone deve conter {min} caracteres")
    @ValidationContactNumber(message = "Telefone inválido. O telefone deve seguir o padrão +XX XX XXXXX-XXXX")
    private final String cellphone;

    @NotEmpty(message = "Senha deve ser preenchido")
    @ValidPassword
    private final String password;

    public Login(String cellphone, String password) {

        this.cellphone = cellphone;
        this.password = password;
        Validation.validate(this);

    }

}
