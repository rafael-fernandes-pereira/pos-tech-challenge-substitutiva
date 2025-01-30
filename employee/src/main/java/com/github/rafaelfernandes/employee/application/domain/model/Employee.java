package com.github.rafaelfernandes.employee.application.domain.model;
import com.github.rafaelfernandes.employee.common.validation.ValidationContactNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import static com.github.rafaelfernandes.employee.common.validation.Validation.validate;

@Getter
public class Employee {

    private final EmployeeId employeeId;

    @Length(min = 3, max = 100, message = "O campo deve ter no minimo {min} e no maximo {max} caracteres")
    private final String name;

    @Size(min = 11, max = 14, message = "Documento deve conter {min} caracteres (sem pontuacao) e {max} (com pontuação)")
    @CPF(message = "CPF inválido")
    private String document;

    @NotNull(message = "Telefone deve ser preenchido")
    @Size(min = 17, max = 17, message = "Telefone deve conter {min} caracteres")
    @ValidationContactNumber(message = "Telefone inválido. O telefone deve seguir o padrão +XX XX XXXXX-XXXX")
    private String cellphone;


    public record EmployeeId(
            @NotEmpty(message = "O campo deve ser do tipo UUID")
            @org.hibernate.validator.constraints.UUID(message = "O campo deve ser do tipo UUID")
            String id){

        public EmployeeId(String id){
            this.id = id;
            validate(this);
        }
    }

    public Employee(String name, String document, String cellphone) {

        this.name = name;
        this.document = document;
        this.cellphone = cellphone;

        validate(this);

        this.employeeId = new EmployeeId(java.util.UUID.randomUUID().toString());

    }

    private Employee(EmployeeId employeeId, String name, String document, String cellphone) {

        this.employeeId = employeeId;
        this.name = name;
        this.document = document;
        this.cellphone = cellphone;

        validate(this);

    }

    public static Employee of(String id, String name, String document, String cellphone) {
        return new Employee(new EmployeeId(id), name, document, cellphone);
    }

}
