import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import model.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@DisplayName("Login user")
public class LoginUserTests {
    private UserClient userClient;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown(){
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Login user by valid data")
    public void userCanBeLoggedWithValidData(){
        User user = User.getRandom();
        Response createResponse = userClient.createUser(user);
        token = createResponse.getBody().path("accessToken");
        Response loginResponse = userClient.loginAsUser(UserCredentials.from(user));

        assertEquals(200, loginResponse.getStatusCode());
    }

    @Test
    @DisplayName("Login user with incorrect email")
    public void userCanNotBeLoggedWithIncorrectEmail(){
        User user = User.getRandom();
        UserCredentials userCredentials = new UserCredentials(user.getEmail() + "a", user.getPassword());
        Response createResponse = userClient.createUser(user);
        token = createResponse.getBody().path("accessToken");
        Response loginResponse = userClient.loginAsUser(userCredentials);

        assertEquals(401, loginResponse.getStatusCode());
        assertEquals("email or password are incorrect", loginResponse.getBody().path("message"));
    }

    @Test
    @DisplayName("Login user with incorrect password")
    public void userCanNotBeLoggedWithIncorrectPassword(){
        User user = User.getRandom();
        UserCredentials userCredentials = new UserCredentials(user.getEmail(), user.getPassword() + "a");
        Response createResponse = userClient.createUser(user);
        token = createResponse.getBody().path("accessToken");
        Response loginResponse = userClient.loginAsUser(userCredentials);

        assertEquals(401, loginResponse.getStatusCode());
        assertEquals("email or password are incorrect", loginResponse.getBody().path("message"));
    }
}
