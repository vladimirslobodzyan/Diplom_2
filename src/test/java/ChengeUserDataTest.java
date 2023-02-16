import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChengeUserDataTest {
    private User user;
    private User updateUser;
    private UserClient userClient;
   private ValidatableResponse response;
    private UserLogin userData;

    private String accessToken;



    @Before
    public void setUp() {
        user = UserGenerator.getDefaultUser();
        updateUser = UserGenerator.getDefaultUser();
        userClient = new UserClient();
        userData = UserGenerator.getNewUser();
        response = userClient.createUser(user);
    }

    @After
    public void cleanUp(){
        String accessToken = response.extract().path("accessToken");
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("User update with authorization")
    public void  userUpdateWithAuth() {
        userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        ValidatableResponse newResponse = userClient.updateUser(accessToken, userData);
        int statusCode = newResponse.extract().statusCode();
        boolean messageResponse = newResponse.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertTrue(messageResponse);
    }
    @Test
    @DisplayName("User update without authorization")
    public void  userUpdateWithOutAuth() {
        userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        ValidatableResponse newResponse = userClient.updateUser("", userData);
        int statusCode = newResponse.extract().statusCode();
        String messageResponse = newResponse.extract().path("message");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertEquals("You should be authorised", messageResponse);
    }
}
