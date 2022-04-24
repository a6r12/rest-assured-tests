import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

public class DemoWebShopApiTests {

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com/";
    }

    @Test
    @Disabled
    void updateBookQuantityCartTest() {

        Integer bookQuantity = 18;

        String response =
                given()
                        .when()
                        .cookie("Nop.customer=45b9315b-73a3-46f2-8a9a-493c30e3cad1;" +
                                "ARRAffinity=1818b4c81d905377ced20e7ae987703a674897394db6e97dc1316168f754a687")
                        .contentType("multipart/form-data")
                        .multiPart("itemquantity2361822", bookQuantity)
                        .multiPart("updatecart", "Update shopping cart")
                        .post("cart")
                        .then()
                        .statusCode(200)
                        .extract().response().asString();

        assertThat(response).contains("class=\"items\">" + bookQuantity + " item(s)</a> in your cart.");

    }

    @Test
    void addToCartTest() {

        given()
                .filter(new AllureRestAssured())
                .log().uri()
                .when()
                .post("addproducttocart/catalog/13/1/1")
                .then()
                .log().status()
                .statusCode(200)
                .body("updatetopcartsectionhtml", is("(1)"));

    }
}
