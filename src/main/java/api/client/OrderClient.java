package api.client;

import api.model.Order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private final String pathCreatedOrder = "/api/orders";
    private final String pathGetIngredients = "/api/ingredients";

    @Step("Create api.model.Order With Authorization")
    public ValidatableResponse createOrderWithAuthorization(String accessToken, Order order) {
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(pathCreatedOrder)
                .then();

    }

    @Step("Create api.model.Order Without Authorization")
    public ValidatableResponse createOrderWithoutAuthorization(String accessToken, Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(pathCreatedOrder)
                .then();
    }

    @Step("Get Orders")
    public ValidatableResponse getOrders(String accessToken) {
        return given()
                .spec(getSpec())
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get(pathCreatedOrder)
                .then();
    }

    @Step("Get Ingredients")
    public ValidatableResponse getIngredients() {
        return given()
                .header("Content-Type", "application/json")
                .spec(getSpec())
                .get(pathGetIngredients)
                .then();
    }
}
