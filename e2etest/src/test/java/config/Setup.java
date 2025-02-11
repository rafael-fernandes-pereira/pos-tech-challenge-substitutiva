package config;


import io.restassured.RestAssured;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;




import java.util.Locale;
import java.util.Random;



public class Setup {

    static final String PORT = "8080";
    static final String BASE_PATH = "api";

    static final String BASE_URI = "http://localhost";

    public static final Faker faker = new Faker(new Locale("pt", "BR"));






    @BeforeAll
    public static void init() {
        RestAssured.port = Integer.parseInt(PORT);
        RestAssured.basePath = BASE_PATH;
        RestAssured.baseURI = BASE_URI;

        RestAssured.requestSpecification = RestAssured.given()
                .header("Content-Type", "application/json");
    }

    public String generateRandomCellphoneNumber() {
        Random random = new Random();

        // Generate the country code (+dd)
        int countryCode = random.nextInt(90) + 1; // Random number between 1 and 90

        // Generate the area code (dd)
        int areaCode = random.nextInt(90) + 10; // Random number between 10 and 99 (2 digits)

        // Generate the first part of the cellphone number (ddddd)
        int firstPart = random.nextInt(90000) + 10000; // Random number between 10000 and 99999 (5 digits)

        // Generate the second part of the cellphone number (dddd)
        int secondPart = random.nextInt(9000) + 1000; // Random number between 1000 and 9999 (4 digits)

        // Format the number as +dd dd ddddd-dddd
        return String.format("+%02d %02d %05d-%04d", countryCode, areaCode, firstPart, secondPart);
    }

    public String token(String cellphone, String password) {

        return RestAssured.given()
                .body(new LoginRequest(cellphone, password))
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }


}
