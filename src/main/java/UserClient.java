import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    private  final String pathCreated = "/api/auth/register";
    private  final String pathLogin = "/api/auth/login";

    private  final String pathAuth = "/api/auth/user";

    @Step ("Create User")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(pathCreated)
                .then();
    }

    @Step ("Login User")
    public ValidatableResponse loginUser(Credentials credentials) {
        return  given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(pathLogin)
                .then();
    }

    @Step ("Update User")
    public ValidatableResponse updateUser(String accessToken, UserLogin userLogin) {
        return  given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(userLogin)
                .when()
                .patch(pathAuth)
                .then();
    }
    @Step ("Delete User")
    public ValidatableResponse deleteUser(String accessToken) {
        return  given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .when()
                .delete(pathAuth)
                .then();
    }
}
