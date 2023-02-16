import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
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
    public void cleanUp(){
        String accessToken = response.extract().path("accessToken");
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }


    @Test
    @DisplayName("User can be created Status 200")
    public void userCanBeCreated(){
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }

    @Test
    @DisplayName("Courier created Message")
    public void userCreatedMessage(){
        boolean messageResponse = response.extract().path("success");
        assertTrue(messageResponse);
    }

    @Test
    @DisplayName("Same user created Status 403")
    public void sameUserCreated(){
        userClient.createUser(user);
        ValidatableResponse response = userClient.createUser(sameUser);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);
    }

    @Test
    @DisplayName("Same user created message")
    public void sameUserCreatedMessage(){
        userClient.createUser(user);
        ValidatableResponse response = userClient.createUser(sameUser);
        String messageResponse = response.extract().path("message");
        assertEquals("User already exists", messageResponse);
    }

    @Test
    @DisplayName("User created without pass")
    public void userWithoutPass(){
        ValidatableResponse response = userClient.createUser(userWithoutPassword);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);

    }

    @Test
    @DisplayName("User created without pass message")
    public void userWithoutPassMessage(){
        ValidatableResponse response = userClient.createUser(userWithoutPassword);
        String messageResponse = response.extract().path("message");
        assertEquals("Email, password and name are required fields", messageResponse);
    }

    @Test
    @DisplayName("User created without email")
    public void userWithoutEmail(){
        ValidatableResponse response = userClient.createUser(userWithoutEmail);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, statusCode);

    }

    @Test
    @DisplayName("User created without email message")
    public void userWithoutEmailMessage(){
        ValidatableResponse response = userClient.createUser(userWithoutEmail);
        String messageResponse = response.extract().path("message");
        assertEquals("Email, password and name are required fields", messageResponse);
    }
    }


