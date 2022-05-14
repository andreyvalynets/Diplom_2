import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@DisplayName("Create user")
public class UserCreateTests {

    private UserClient userClient;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Create user")
    public void userCanBeCreatedWithValidDataTest(){
        User user =  User.getRandom();
        Response response = userClient.createUser(user);
        token = response.getBody().path("accessToken");

        assertEquals("User is not created",200, response.getStatusCode());
        assertTrue(response.getBody().path("success"));
    }

    @Test
    @DisplayName("Create two identical users")
    public void canNotBeCreatedTwoIdenticalUsers(){
        User user =  User.getRandom();
        Response firstResponse = userClient.createUser(user);
        Response secondResponse = userClient.createUser(user);
        token = firstResponse.getBody().path("accessToken");

        assertEquals("Two identical users are created", 403, secondResponse.getStatusCode());
        assertFalse(secondResponse.getBody().path("success"));
        assertEquals("User already exists", secondResponse.getBody().path("message"));
    }

}
