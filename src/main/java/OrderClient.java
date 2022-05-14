import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    private final String PATH = BASE_URL + "api/orders/";

    @Step("Create order")
    public Response createOrder(Order order, String token) {
        if (token == null) {
            return given()
                    .spec(getBaseSpec())
                    .body(order)
                    .when()
                    .post(PATH);
        }
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(PATH);
    }

    @Step("Get order")
    public Response getOrder() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(BASE_URL + "api/ingredients/");
    }

    @Step("Get order for user")
    public Response getOrderForUser(String token) {
        if (token == null) {
            return given()
                    .spec(getBaseSpec())
                    .when()
                    .get(PATH);
        }
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .when()
                .get(PATH);
    }

}
