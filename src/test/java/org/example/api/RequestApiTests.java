package org.example.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.model.CreateUserRequest;
import org.example.model.CreateUserResponse;
import org.junit.jupiter.api.Test;
import org.example.setup.TestBase;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RequestApiTests extends TestBase {

    public RequestApiTests() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    public void testGetUsersPage2() {
        given()
                .queryParam("page", 2)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data", hasSize(6))
                .body("data[0].email", equalTo("michael.lawson@reqres.in"));
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    public void testGetUsersInvalidPage() {
        given()
                .when()
                .get("/api/users/23")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
    }


    @Test
    public void testCreateUser() {
        CreateUserRequest request = new CreateUserRequest("morpheus", "leader");

        Response response = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().response();

        CreateUserResponse userResponse = response.as(CreateUserResponse.class);

        assert userResponse.getName().equals("morpheus");
        assert userResponse.getJob().equals("leader");
    }


    @Test
    public void testUpdateUserPut() {
        CreateUserRequest request = new CreateUserRequest("morpheus", "zion resident");

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"));
    }


    @Test
    public void testUpdateUserPatch() {
        CreateUserRequest request = new CreateUserRequest("morpheus", "zion resident");

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .patch("/api/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"));
    }


    @Test
    public void testDeleteUser() {
        given()
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204);
    }
}
