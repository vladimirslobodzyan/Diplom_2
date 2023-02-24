import api.client.OrderClient;
import api.client.UserClient;
import api.model.Order;
import api.model.User;
import api.util.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateOrderTest {
    private User user;
    private UserClient userClient;
    private String accessToken;
    private List<String> ingredients;
    private Order order;

    private OrderClient orderClient;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        user = UserGenerator.getDefaultUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        accessToken = userClient.createUser(user)
                .extract().path("accessToken");
        response = userClient.createUser(user);
        order = new Order(ingredients);
        ingredients = orderClient.getIngredients()
                .extract().path("data._id");
    }

    @After
    public void delete() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Create order with authorization")
    public void createOrderWithAuthorizationTest() {
        Order order = new Order(ingredients);
        ValidatableResponse newResponse = orderClient.createOrderWithAuthorization(accessToken, order);
        int statusCode = newResponse.extract().statusCode();
        boolean messageResponse = newResponse.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertTrue(messageResponse);
    }

    @Test
    @DisplayName("Create order without authorization")
    public void createOrderWithoutAuthorizationTest() {
        Order order = new Order(ingredients);
        accessToken = "";
        ValidatableResponse newResponse = orderClient.createOrderWithoutAuthorization(accessToken, order);
        int statusCode = newResponse.extract().statusCode();
        boolean messageResponse = newResponse.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertTrue(messageResponse);
    }

    @Test
    @DisplayName("Create order with ingredients")
    public void createOrderWithIngredientsTest() {
        Order order = new Order(ingredients);
        ValidatableResponse newResponse = orderClient.createOrderWithAuthorization(accessToken, order);
        int statusCode = newResponse.extract().statusCode();
        boolean booleanResponse = newResponse.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertTrue(booleanResponse);
    }

    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderWithoutIngredientsTest() {
        ingredients = emptyList();
        Order order = new Order(ingredients);
        ValidatableResponse newResponse = orderClient.createOrderWithAuthorization(accessToken, order);
        int statusCode = newResponse.extract().statusCode();
        boolean booleanResponse = newResponse.extract().path("success");
        String messageResponse = newResponse.extract().path("message");
        assertEquals(SC_BAD_REQUEST, statusCode);
        assertFalse(booleanResponse);
        assertEquals("Ingredient ids must be provided", messageResponse);
    }

    @Test
    @DisplayName("Create order with incorrect hash")
    public void createOrderWithIncorrectHashTest() {
        ingredients = List.of("incorrectHashIngredients", "incorrectHashIngredients");
        Order order = new Order(ingredients);
        ValidatableResponse newResponse = orderClient.createOrderWithAuthorization(accessToken, order);
        int statusCode = newResponse.extract().statusCode();
        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }
}
