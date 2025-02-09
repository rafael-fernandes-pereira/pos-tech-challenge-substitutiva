package com.github.rafaelfernandes.delivery.application.domain.model;

import com.github.rafaelfernandes.delivery.common.validation.ValidationContactNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import static com.github.rafaelfernandes.delivery.common.validation.Validation.validate;


@Getter
public class Resident {

    private final ResidentId residentId;

    @Length(min = 3, max = 100, message = "O campo deve ter no minimo {min} e no maximo {max} caracteres")
    private final String name;

    @Size(min = 11, max = 14, message = "Documento deve conter {min} caracteres (sem pontuacao) e {max} (com pontuação)")
    @CPF(message = "CPF inválido")
    private String document;

    @NotNull(message = "Telefone deve ser preenchido")
    @Size(min = 17, max = 17, message = "Telefone deve conter {min} caracteres")
    @ValidationContactNumber(message = "Telefone inválido. O telefone deve seguir o padrão +XX XX XXXXX-XXXX")
    private String cellphone;

    @NotNull(message = "O campo deve ser maior que zero (0)")
    @Positive(message = "O campo deve ser maior que zero (0)")
    private final Integer apartment;



    public record ResidentId(
            @NotEmpty(message = "O campo deve ser do tipo UUID")
            @org.hibernate.validator.constraints.UUID(message = "O campo deve ser do tipo UUID")
            String id){

        public ResidentId(String id){
            this.id = id;
            validate(this);
        }
    }

    public Resident(String name, String document, String cellphone, Integer apartment) {

        this.name = name;
        this.document = document;
        this.cellphone = cellphone;
        this.apartment = apartment;

        validate(this);

        this.residentId = new ResidentId(java.util.UUID.randomUUID().toString());

    }

    private Resident(ResidentId residentId, String name, String document, String cellphone, Integer apartment) {

        this.residentId = residentId;
        this.name = name;
        this.document = document;
        this.cellphone = cellphone;
        this.apartment = apartment;

        validate(this);

    }

    public static Resident of(String id, String name, String document, String cellphone, Integer apartment) {
        return new Resident(new ResidentId(id), name, document, cellphone, apartment);
    }

}
