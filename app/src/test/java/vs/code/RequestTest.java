package vs.code;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

public class RequestTest {

    @BeforeEach
    public void setUp() {

        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void testLogin() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "   \"email\": \"eve.holt@reqres.in\", \n" +
                        "   \"password\": \"pistol\"\n"
                        +
                        "}")
                .post("/register")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("token", notNullValue());

        // System.out.println(response);

    }

    @Test
    public void singleUser() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .get("/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("data.first_name", equalTo("Janet"));

    }

    @Test
    public void updateUSer() {

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "       \"name\": \"neo\",  \n" +
                        "       \"job\": \"zion resident\" \n" +
                        "}")
                .put("/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("neo"));
    }

    @Test
    public void parcialUpdateUSer() {

       String jobUpdate = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "       \"job\": \"zion dead resident\" \n" +
                        "}")
                .patch("/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath().getString("job");

                assertThat(jobUpdate, equalTo("zion dead resident"));        

                

    }

    @Test
    public void eliminarUser() {

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .delete("/users/2")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

    }

}
