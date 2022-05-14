import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;
import model.UserCredentials;
import model.UserUpdate;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient{

    private final String PATH = BASE_URL + "api/auth/";

    @Step("Create ")
    public Response createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(PATH + "register");
    }

    @Step("Login user")
    public Response loginAsUser(UserCredentials userCredentials){
        return given()
                .spec(getBaseSpec())
                .body(userCredentials)
                .when()
                .post(PATH + "login/");
    }

    @Step("Delete user")
    public Response deleteUser(String token){
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .when()
                .delete(PATH + "user/");
    }
    @Step("Edit user")
    public Response editUser(UserUpdate userUpdate, String token) {
        if(token == null){
            return given()
                    .spec(getBaseSpec())
                    .body(userUpdate)
                    .when()
                    .patch(PATH + "user");
        } return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .body(userUpdate)
                .when()
                .patch(PATH + "user");
    }

}
