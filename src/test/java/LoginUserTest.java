import api.client.UserClient;
import api.model.Credentials;
import api.model.User;
import api.util.InvalidUserGenerator;
import api.util.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginUserTest {

    private User user;
    private UserClient userClient;
    private Credentials userCredentialsWithoutPassword;
    private Credentials userCredentialsWithoutEmail;
    private ValidatableResponse response;
    private Credentials invalidUserCredentials;

    @Before
    public void setUp() {
        user = UserGenerator.getDefaultUser();
        userClient = new UserClient();
        userCredentialsWithoutPassword = new Credentials(user.getEmail(), "");
        userCredentialsWithoutEmail = new Credentials("", user.getPassword());
        invalidUserCredentials = InvalidUserGenerator.getInvalidUser();
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
    @DisplayName("api.model.User can be login")
    public void userCanBeLogin() {
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(Credentials.from(user));
        int statusCode = loginResponse.extract().statusCode();
        boolean messageResponse = loginResponse.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertTrue(messageResponse);
    }


    @Test
    @DisplayName("api.model.User credential without password")
    public void userCredentialsWithoutPassword() {
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(userCredentialsWithoutPassword);
        int statusCode = loginResponse.extract().statusCode();
        String messageResponse = loginResponse.extract().path("message");
        assertEquals("email or password are incorrect", messageResponse);
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }


    @Test
    @DisplayName("api.model.User credentials without login")
    public void userCredentialsWithoutLogin() {
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(userCredentialsWithoutEmail);
        int statusCode = loginResponse.extract().statusCode();
        String messageResponse = loginResponse.extract().path("message");
        assertEquals("email or password are incorrect", messageResponse);
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }


    @Test
    @DisplayName("Invalid user credentials")
    public void invalidUserCredentials() {
        userClient.createUser(user);
        ValidatableResponse loginResponse = userClient.loginUser(invalidUserCredentials);
        int statusCode = loginResponse.extract().statusCode();
        String messageResponse = loginResponse.extract().path("message");
        assertEquals("email or password are incorrect", messageResponse);
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }

}
