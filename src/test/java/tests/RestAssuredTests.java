package tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static listeners.CustomAllureListener.withCustomTemplates;
import static org.hamcrest.Matchers.*;

public class RestAssuredTests {

    String baseUrl = "https://reqres.in/";

    @Test
    void listUsersTest() {
        given()
                .filter(withCustomTemplates())
                .when()
                .get(baseUrl + "api/users?page=2")
                .then()
                .statusCode(200)
                .body("data.email", hasItem("michael.lawson@reqres.in"));
    }

    @Test
    void createUserTest() {
        String jsonString = "{\"name\": \"Artem\", \"job\": \"QAEngineer\"}";
        given()
                .filter(withCustomTemplates())
                .body(jsonString)
                .contentType(ContentType.JSON)
                .when()
                .post(baseUrl + "api/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("Artem"))
                .body("job", equalTo("QAEngineer"))
                .body("createdAt", is(notNullValue()));
    }

    @Test
    void updateUserTest() {
        String jsonString = "{\"name\": \"Artem\", \"job\": \"QAEngineer\"}";
        given()
                .filter(withCustomTemplates())
                .body(jsonString)
                .contentType(ContentType.JSON)
                .when()
                .put(baseUrl + "api/users/6")
                .then()
                .statusCode(200)
                .body("name", equalTo("Artem"));
    }

    @Test
    void deleteUserTest() {
        given()
                .filter(withCustomTemplates())
                .when()
                .delete(baseUrl + "api/users/6")
                .then()
                .statusCode(204);
    }

    @Test
    void errorRegisterUserTest() {
        String jsonString = "{\"emil\": \"sydneyy@fife\"}";
        given()
                .filter(withCustomTemplates())
                .body(jsonString)
                .contentType(ContentType.JSON)
                .when()
                .post(baseUrl + "api/register")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing email or username"));
    }
}

