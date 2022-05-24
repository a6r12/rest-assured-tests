package tests;

import io.restassured.http.ContentType;
import models.ClientProperties;
import models.CreateUserResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static listeners.CustomAllureListener.withCustomTemplates;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class RestAssuredTests {

    String baseUrl = "https://reqres.in/";

    @Test
    void createUserTest() {
        ClientProperties properties = new ClientProperties();
        properties.setName("katana_sword_party");
        properties.setJob("QA Engineer");

        CreateUserResponse createUserResponse = given()
                .filter(withCustomTemplates())
                .body(properties)
                .contentType(ContentType.JSON)
                .when()
                .post(baseUrl + "api/users")
                .then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/CreateUser_generate_schema.json"))
                .extract().as(CreateUserResponse.class);


        assertThat(createUserResponse.getName()).isEqualTo("katana_sword_party");
        assertThat(createUserResponse.getJob()).isEqualTo("QA Engineer");
        assertThat(createUserResponse.getId()).hasSizeGreaterThan(1);
        assertThat(createUserResponse.getCreatedAt()).isNotNull();
    }

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
    void updateUserTest() {
        ClientProperties properties = new ClientProperties();
        properties.setName("katana_sword_party");
        properties.setJob("QA Engineer");

        given()
                .filter(withCustomTemplates())
                .body(properties)
                .contentType(ContentType.JSON)
                .when()
                .put(baseUrl + "api/users/6")
                .then()
                .statusCode(200)
                .body("name", equalTo("katana_sword_party"));
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

