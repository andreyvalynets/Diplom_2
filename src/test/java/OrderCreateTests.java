import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@DisplayName("Create order")
public class OrderCreateTests {

    private UserClient userClient;
    private OrderClient orderClient;
    private List<Object> ingredients;
    private String token;
    private final List<Object> wrongIngredients = new ArrayList<>();

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
    @DisplayName("Create order by authorized user")
    public void orderCanBeCreatedWithAuthorizedUser() {
        User user = User.getRandom();
        Order order = new Order(ingredients);
        Response createUserResponse = userClient.createUser(user);
        token = createUserResponse.getBody().path("accessToken");
        Response createOrderResponse = orderClient.createOrder(order, token);


        assertEquals("Order is not created", 200, createOrderResponse.getStatusCode());
        assertTrue(createOrderResponse.getBody().path("success"));
    }

    @Test
    @DisplayName("Create order by authorized user")
    public void orderCanBeCreatedWithNonAuthorizedUser() {
        User user = User.getRandom();
        Order order = new Order(ingredients);
        Response createUserResponse = userClient.createUser(user);
        token = createUserResponse.getBody().path("accessToken");
        Response createOrderResponse = orderClient.createOrder(order, null);


        assertEquals("Order is not created", 200, createOrderResponse.getStatusCode());
        assertTrue(createOrderResponse.getBody().path("success"));
    }

    @Test
    @DisplayName("Create order without ingredients")
    public void orderCanNotBeCreatedWithoutIngredients() {
        User user = User.getRandom();
        Order order = new Order(null);
        Response createUserResponse = userClient.createUser(user);
        token = createUserResponse.getBody().path("accessToken");
        Response createOrderResponse = orderClient.createOrder(order, token);


        assertEquals("Order is created", 400, createOrderResponse.getStatusCode());
        assertFalse(createOrderResponse.getBody().path("success"));
        assertEquals("Ingredient ids must be provided", createOrderResponse.getBody().path("message"));
    }

    @Test
    @DisplayName("Create order with wrong ingredients")
    public void orderCanNotBeCreatedWithWrongIngredients() {
        User user = User.getRandom();
        Order order = new Order(wrongIngredients);
        Response createUserResponse = userClient.createUser(user);
        token = createUserResponse.getBody().path("accessToken");
        wrongIngredients.add("123");
        Response createOrderResponse = orderClient.createOrder(order, token);

        assertEquals("Order is created with invalid ingredient", 500, createOrderResponse.getStatusCode());
    }
}
