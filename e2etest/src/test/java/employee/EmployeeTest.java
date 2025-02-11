package employee;

import config.Setup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import resident.ResidentRequest;
import config.UserCreatedResponse;

import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EmployeeTest extends Setup {

    private static final Logger LOGGER = Logger.getLogger( EmployeeTest.class.getName() );

    @Test
    @DisplayName("Criar, verificar o registro, alterar e deletar um porteiro")
    void createResident() {

        LOGGER.info("Criando um porteiro");

        var request = new EmployeeRequest(
                Setup.faker.name().fullName(),
                Setup.faker.cpf().valid(),
                generateRandomCellphoneNumber()
        );

        var response = given()
            .body(request)
        .when()
            .post("/user/employee")
        .then()
            .log().all()
            .statusCode(200)
            .body("cellphone", equalTo(request.cellphone()))
        .extract()
        .as(UserCreatedResponse.class);

        LOGGER.info("Obtendo o token do porteiro");

        var token = token(request.cellphone(), response.password());

        LOGGER.info("Obtendo dados do porteiro pelo celular");

        var id =  given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/employee/cellphone/{cellphone}", request.cellphone())
                .then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo(request.name()))
                .body("document", equalTo(request.document()))
                .body("cellphone", equalTo(request.cellphone()))
                .extract()
                .jsonPath()
                .get("id");


        LOGGER.info("Obtendo dados do porteiro pelo id");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/employee/{id}", id)
                .then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo(request.name()))
                .body("document", equalTo(request.document()))
                .body("cellphone", equalTo(request.cellphone()))

        ;

        LOGGER.info("Obtendo todos os porteiros");

        var cellphones = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/employee?page=0&size=1000")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("content.cellphone", String.class);

        assertThat(cellphones, hasItem(containsString(request.cellphone())));

        LOGGER.info("Alterando dados do porteiro");

        var updatedRequest = new EmployeeRequest(
                Setup.faker.name().fullName(),
                request.document(),
                request.cellphone()
        );

        given()
                .header("Authorization", "Bearer " + token)
                .body(updatedRequest)
                .when()
                .put("/employee/{id}", id)
                .then()
                .log().all()
                .statusCode(200);

        LOGGER.info("Deletando o porteiro");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/employee/{id}", id)
                .then()
                .log().all()
                .statusCode(200);

    }

}
