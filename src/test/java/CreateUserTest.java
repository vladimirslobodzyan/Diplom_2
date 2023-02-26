import api.client.UserClient;
import api.model.User;
import api.util.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateUserTest {

    private User user;

    private UserClient userClient;

    private ValidatableResponse response;

    private User userWithoutPassword;
    private User userWithoutEmail;
    private User sameUser;

    @Before
    public void setUp() {
        user = UserGenerator.getDefaultUser();
        userClient = new UserClient();
        sameUser = new User(user.getEmail(), user.getPassword(), user.getName());
        userWithoutPassword = UserGenerator.getDefaultUserWithoutPass();
        userWithoutEmail = UserGenerator.getDefaultUserWithoutEmail();
        response = userClient.createUser(user);
    }

    @After
    public void cleanUp() {
        String accessToken = response.extract().path("accessToken");
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("api.model.User can be created Status 200")
    public void userCanBeCreated() {
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }

    @Test
    @DisplayName("Courier created Message")
    public void userCreatedMessage() {
        boolean messageResponse = response.extract().path("success");
        assertTrue(messageResponse);
    }

    @Test
    @DisplayName("Same user created test")
    public void sameUserCreated() {
        userClient.createUser(user);
        ValidatableResponse response = userClient.createUser(sameUser);
        int statusCode = response.extract().statusCode();
        String messageResponse = response.extract().path("message");
        assertEquals("User already exists", messageResponse);
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @Test
    @DisplayName("api.model.User created without pass test")
    public void userWithoutPass() {
        ValidatableResponse response = userClient.createUser(userWithoutPassword);
        int statusCode = response.extract().statusCode();
        String messageResponse = response.extract().path("message");
        assertEquals("Email, password and name are required fields", messageResponse);
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @Test
    @DisplayName("api.model.User created without email test")
    public void userWithoutEmail() {
        ValidatableResponse response = userClient.createUser(userWithoutEmail);
        int statusCode = response.extract().statusCode();
        String messageResponse = response.extract().path("message");
        assertEquals("Email, password and name are required fields", messageResponse);
        assertEquals(SC_FORBIDDEN, statusCode);
    }

}


