import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
@DisplayName("Create user")
public class UserCreateWithoutParametersTests {

    private final String email;
    private final String password;
    private final String name;
    private UserClient userClient;

    public UserCreateWithoutParametersTests(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {"", "123321123", "AnyName"},
                {null, "123321123", "AnyName"},
                {"test1@testmail.com", "", "AnyName"},
                {"test2@testmail.com", null, "AnyName"},
                {"test3@testmail.com", "123321123", ""},
                {"test4@testmail.com", "123321123", null}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Create user without fields")
    public void cannotBeCreatedUserWithoutRequiredFields() {
        User user = new User(email, password, name);
        Response response = userClient.createUser(user);

        assertEquals("User is created without required field", 403, response.getStatusCode());
        assertEquals("Email, password and name are required fields", response.getBody().path("message"));
    }
}
