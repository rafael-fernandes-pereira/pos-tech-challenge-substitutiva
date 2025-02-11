package resident;

import config.Setup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ResidentTest extends Setup {

    private static final Logger LOGGER = Logger.getLogger( ResidentTest.class.getName() );

    @Test
    @DisplayName("Criar, verificar o registro, alterar e deletar um residente")
    void createResident() {

        LOGGER.info("Criando um residente");

        var request = new ResidentRequest(
                Setup.faker.name().fullName(),
                Setup.faker.cpf().valid(),
                generateRandomCellphoneNumber(),
                Setup.faker.number().positive()
        );

        var response = given()
            .body(request)
        .when()
            .post("/user/resident")
        .then()
            .log().all()
            .statusCode(200)
            .body("cellphone", equalTo(request.cellphone()))
        .extract()
        .as(UserCreatedResponse.class);

        LOGGER.info("Obtendo o token do residente");

        var token = token(request.cellphone(), response.password());

        LOGGER.info("Obtendo dados do residente pelo celular");

        var ids =  given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/resident/cellphone/{cellphone}", request.cellphone())
        .then()
            .log().all()
            .statusCode(200)
            .body("name", contains(request.name()))
            .body("document", contains(request.document()))
            .body("cellphone", contains(request.cellphone()))
            .body("apartment", contains(request.apartment()))
        .extract()
                .jsonPath()
                .getList("id", String.class);


        var id = ids.get(0);

        LOGGER.info("Obtendo dados do residente pelo id");

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/resident/{id}", id)
        .then()
            .log().all()
            .statusCode(200)
            .body("name", equalTo(request.name()))
            .body("document", equalTo(request.document()))
            .body("cellphone", equalTo(request.cellphone()))
            .body("apartment", equalTo(request.apartment()));


        LOGGER.info("Obtendo através do numero do apartamento");

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/resident/apartment/{apartment}", request.apartment())
        .then()
            .log().all()
            .statusCode(200)
            .body("name", equalTo(request.name()))
            .body("document", equalTo(request.document()))
            .body("cellphone", equalTo(request.cellphone()))
            .body("apartment", equalTo(request.apartment()));


        LOGGER.info("Obtendo todos os residentes");

        var cellphones = given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/resident?page=0&size=1000")
        .then()
            .log().all()
            .statusCode(200)
        .extract()
            .jsonPath()
            .getList("content.cellphone", String.class);

        assertThat(cellphones, hasItem(containsString(request.cellphone())));

        LOGGER.info("Alterando dados do residente");

        var updatedRequest = new ResidentRequest(
                Setup.faker.name().fullName(),
                request.document(),
                request.cellphone(),
                request.apartment()
        );

        given()
                .header("Authorization", "Bearer " + token)
                .body(updatedRequest)
                .when()
                .put("/resident/{id}", id)
                .then()
                .log().all()
                .statusCode(200);

        LOGGER.info("Deletando o residente");

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .delete("/resident/{id}", id)
        .then()
            .log().all()
            .statusCode(200);

    }

}
