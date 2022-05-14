import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

@DisplayName("Get list of orders for user")
public class GetOrdersTests {

    private UserClient userClient;
    private OrderClient orderClient;
    private List<Object> ingredients;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        ingredients = orderClient.getOrder().getBody().path("data._id");

    }

    @After
    public void tearDown() {
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Get list of orders for authorized user")
    public void orderCanBeDisplayedForAuthorizedUser() {
        User user = User.getRandom();
        Order order = new Order(ingredients);
        Response createUserResponse = userClient.createUser(user);
        token = createUserResponse.getBody().path("accessToken");
        orderClient.createOrder(order, token);
        Response getOrdersResponse = orderClient.getOrderForUser(token);


        assertEquals("Orders are not displayed", 200, getOrdersResponse.getStatusCode());
        assertTrue(getOrdersResponse.getBody().path("success"));
    }

    @Test
    @DisplayName("Get list of orders for non-authorized user")
    public void orderCanNotBeDisplayedForNonAuthorizedUser() {
        User user = User.getRandom();
        Order order = new Order(ingredients);
        Response createUserResponse = userClient.createUser(user);
        token = createUserResponse.getBody().path("accessToken");
        orderClient.createOrder(order, token);
        Response getOrdersResponse = orderClient.getOrderForUser(null);


        assertEquals("Orders are not displayed", 401, getOrdersResponse.getStatusCode());
        assertFalse(getOrdersResponse.getBody().path("success"));
        assertEquals("You should be authorised", getOrdersResponse.getBody().path("message"));
    }
}
