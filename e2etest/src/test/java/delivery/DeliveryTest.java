package delivery;

import config.Setup;
import config.UserCreatedResponse;
import employee.EmployeeRequest;
import org.junit.jupiter.api.Test;
import resident.ResidentRequest;

import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DeliveryTest extends Setup {

    private static final Logger LOGGER = Logger.getLogger( DeliveryTest.class.getName() );

    @Test
    void delivery(){

        LOGGER.info("Registrar um novo funcionário");

        var requestEmployee = new EmployeeRequest(
                Setup.faker.name().fullName(),
                Setup.faker.cpf().valid(),
                generateRandomCellphoneNumber()
        );

        var employee = given()
                .body(requestEmployee)
                .when()
                .post("/user/employee")
                .then()
                .log().all()
                .statusCode(200)
                .body("cellphone", equalTo(requestEmployee.cellphone()))
                .extract()
                .as(UserCreatedResponse.class);

        LOGGER.info("Funcionário registrado com sucesso");

        LOGGER.info("Registrar um novo morador");

        var residentRequest = new ResidentRequest(
                Setup.faker.name().fullName(),
                Setup.faker.cpf().valid(),
                generateRandomCellphoneNumber(),
                Setup.faker.number().numberBetween(1, 10000)
        );

        var resident = given()
                .body(residentRequest)
                .when()
                .post("/user/resident")
                .then()
                .log().all()
                .statusCode(200)
                .body("cellphone", equalTo(residentRequest.cellphone()))
                .extract()
                .as(UserCreatedResponse.class);

        LOGGER.info("Morador registrado com sucesso");

        LOGGER.info("Registrar uma entrega");

        var tokenEmployee = token(requestEmployee.cellphone(), employee.password());

        var deliveryRequest = new DeliveryRequest(
                residentRequest.apartment(),
                requestEmployee.cellphone(),
                faker.name().fullName(),
                faker.beer().name()
        );

        var deliveryId = given()
                .header("Authorization", "Bearer " + tokenEmployee)
                .body(deliveryRequest)
        .when()
                .post("/delivery/register")
        .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .get("id");

        LOGGER.info("Entrega id: " + deliveryId);

        LOGGER.info("Verificando se a entrega está pendente");

        var allDeliveries = given()
                .header("Authorization", "Bearer " + tokenEmployee)
        .when()
                .get("/delivery/apartment/{apartment}", residentRequest.apartment())
        .then()
                .log().all()
                .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList(".", DeliveryResponse.class);

        var delivery = allDeliveries.stream().filter(d -> d.id().equals(deliveryId)).findFirst().orElseThrow();

        assertThat(delivery.deliveryStatus(), equalTo("TO_DELIVER"));

        LOGGER.info("Morador leu a notificação");

        var tokenResident = token(residentRequest.cellphone(), resident.password());

        given()
                .header("Authorization", "Bearer " + tokenResident)
        .when()
                .put("/delivery/{deliveryId}/notification/read", deliveryId)
        .then()
                .log().all()
                .statusCode(200);

        LOGGER.info("Pacote entregue");

        var deliveredRequest = new DeliveredRequest(
                faker.name().fullName()
        );

        given()
                .header("Authorization", "Bearer " + tokenEmployee)
                .body(deliveredRequest)
        .when()
                .put("/delivery/{deliveryId}/delivered", deliveryId)
        .then()
                .log().all()
                .statusCode(200);

        allDeliveries = given()
                .header("Authorization", "Bearer " + tokenEmployee)
                .when()
                .get("/delivery/apartment/{apartment}", residentRequest.apartment())
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", DeliveryResponse.class);

        delivery = allDeliveries.stream().filter(d -> d.id().equals(deliveryId)).findFirst().orElseThrow();

        assertThat(delivery.deliveryStatus(), equalTo("DELIVERED"));


    }

}
