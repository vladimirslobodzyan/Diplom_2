import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.*;

import static org.junit.Assert.*;

public class GetOrderTest {

    private User user;
    private UserClient userClient;
    private String accessToken;
    private List<String> ingredients;
    private Order order;

    private  OrderClient orderClient;
    private ValidatableResponse response;



    @Before
    public void setUp() {
        user = UserGenerator.getDefaultUser();;
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
    @DisplayName("Get authorization orders test")
    public void getAuthUserOrdersTest() {
        ValidatableResponse newResponse = orderClient.getOrders(accessToken);
        int statusCode = newResponse.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }

    @Test
    @DisplayName("Get user order without authorization ")
    public void getUserOrdersWithoutTest() {
        ValidatableResponse newResponse = orderClient.getOrders("");
        int statusCode = newResponse.extract().statusCode();
        boolean booleanResponse = newResponse.extract().path("success");
        String messageResponse = newResponse.extract().path("message");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(booleanResponse);
        assertEquals("You should be authorised", messageResponse);
    }
}
