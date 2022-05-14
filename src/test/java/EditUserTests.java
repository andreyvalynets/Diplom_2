import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import model.UserCredentials;
import model.UserUpdate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@DisplayName("Edit user")
public class EditUserTests {

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
    @DisplayName("Edit authorized user")
    public void userCanBeUpdatedWhenAuthorized() {
        User user = User.getRandom();
        UserUpdate userUpdate = new UserUpdate("edit_email@yandex.ru", "some_name");
        Response createResponse = userClient.createUser(user);
        token = createResponse.getBody().path("accessToken");
        userClient.loginAsUser(UserCredentials.from(user));
        Response editResponse = userClient.editUser(userUpdate, token);

        assertEquals("User is not updated", 200, editResponse.getStatusCode());
        assertTrue(editResponse.getBody().path("success"));
    }

    @Test
    @DisplayName("Edit non-authorized user")
    public void userCanNotBeUpdatedWhenNonAuthorized() {
        User user = User.getRandom();
        UserUpdate userUpdate = new UserUpdate("edit_email@yandex.ru", "some_name");
        Response createResponse = userClient.createUser(user);
        token = createResponse.getBody().path("accessToken");
        Response editResponse = userClient.editUser(userUpdate, null);

        assertEquals("User is updated", 401, editResponse.getStatusCode());
        assertFalse(editResponse.getBody().path("success"));
        assertEquals("You should be authorised", editResponse.getBody().path("message"));
    }

}
