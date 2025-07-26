package APITests;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

@Epic("DummyJSON")
@Feature("Auth API")
public class AuthTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://dummyjson.com";
    }

    @Test(description = "Login exitoso de usuario", groups = {"auth"})
    @Severity(SeverityLevel.CRITICAL)
    @Owner("JuanMartin")
    @Description("POST /auth/login debe devolver status 200 y un token")
    public void testLoginSuccess() {
        String body = """
        {
            "username": "oliviaw",
            "password": "oliviawpass"
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(201)// Cambiado a 201 para probar allure
                .body("accessToken", notNullValue())
                .body("username", equalTo("oliviaw"));
    }

    @Test(description = "Login inválido", groups = {"auth"})
    @Severity(SeverityLevel.NORMAL)
    @Owner("JuanMartin")
    @Description("POST /auth/login con credenciales inválidas debe dar 400")
    public void testLoginFail() {
        String body = """
        {
            "username": "usuario_invalido",
            "password": "password_invalido"
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(400)
                .body("message", containsString("Invalid credentials"));
    }
}
