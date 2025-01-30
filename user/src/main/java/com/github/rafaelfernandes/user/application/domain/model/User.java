package com.github.rafaelfernandes.user.application.domain.model;

import com.github.rafaelfernandes.common.enums.UserType;
import com.github.rafaelfernandes.common.utils.PasswordUtils;
import com.github.rafaelfernandes.common.validation.ValidPassword;
import com.github.rafaelfernandes.common.validation.Validation;
import com.github.rafaelfernandes.common.validation.ValidationContactNumber;
import com.github.rafaelfernandes.common.validation.ValueOfEnum;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Optional;
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

    private Optional<Resident> resident;
    private Optional<Employee> employee;

    public record UserId(String id) {
        public UserId(String id) {
            this.id = id;
            Validation.validate(this);
        }
    }

    public User(Resident resident) {

        this.userType = UserType.RESIDENT.name();
        this.resident = Optional.of(resident);

        this.cellphone = resident.getCellphone();

        this.password = PasswordUtils.generatePassayPassword();
        Validation.validate(this);

        var id = UUID.randomUUID();

        this.userId = new UserId(id.toString());

    }

    public User(Employee employee) {

        this.userType = UserType.EMPLOYEE.name();
        this.employee = Optional.of(employee);

        this.cellphone = employee.getCellphone();

        this.password = PasswordUtils.generatePassayPassword();
        Validation.validate(this);

        var id = UUID.randomUUID();

        this.userId = new UserId(id.toString());

    }

    private User(UserId userId, String userType, String cellphone, String password) {
        this.userId = userId;
        this.userType = userType;
        this.password = password;
        this.cellphone = cellphone;

        Validation.validate(this);
    }

    public static User of(String userId, String userType, String cellphone, String password) {
        var id = new UserId(userId);
        return new User(id, userType, cellphone, password);
    }
}
