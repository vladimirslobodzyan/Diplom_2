package api.client;

import api.model.Credentials;
import api.model.User;
import api.model.UserLogin;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private final String pathCreated = "/api/auth/register";
    private final String pathLogin = "/api/auth/login";

    private final String pathAuth = "/api/auth/user";

    @Step("Create api.model.User")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(pathCreated)
                .then();
    }

    @Step("Login api.model.User")
    public ValidatableResponse loginUser(Credentials credentials) {
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(pathLogin)
                .then();
    }

    @Step("Update api.model.User")
    public ValidatableResponse updateUser(String accessToken, UserLogin userLogin) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(userLogin)
                .when()
                .patch(pathAuth)
                .then();
    }

    @Step("Delete api.model.User")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .when()
                .delete(pathAuth)
                .then();
    }
}
