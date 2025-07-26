package APITests;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

@Epic("DummyJSON")
@Feature("Users API")
public class UsersTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://dummyjson.com";
    }

    @Test(description = "Validar schema de respuesta para un usuario")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("JuanMartin")
    @Description("GET /users/1 debe cumplir el contrato users-schema.json")
    @Link("https://dummyjson.com/docs/users#users-single")
    public void validateUserSchema() {
        given()
        .when()
            .get("/users/1")
        .then()
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test(description = "Obtener todos los usuarios", groups = {"users", "regression"})
    @Severity(SeverityLevel.NORMAL)
    @Owner("JuanMartin")
    @Description("GET /users devuelve una lista de usuarios")
    public void testGetAllUsers() {
        given()
            .when()
            .get("/users")
            .then()
            .statusCode(200)
            .body("users", notNullValue())
            .body("users[0]", hasKey("id"))
            .body("users[0]", hasKey("firstName"))
            .body("users[0]", hasKey("email"))
            .body("total", greaterThan(0));
    }

    @Test(description = "Obtener usuario por ID", groups = {"users"})
    @Severity(SeverityLevel.NORMAL)
    @Owner("JuanMartin")
    @Description("GET /users/{id} devuelve el usuario correcto")
    public void testGetUserById() {
        int userId = 1;
        given()
            .when()
            .get("/users/{id}", userId)
            .then()
            .statusCode(200)
            .body("id", equalTo(userId))
            .body("firstName", notNullValue())
            .body("email", notNullValue());
    }

    @Test(description = "Buscar usuarios con 'John' en el nombre o apellido", groups = {"users", "search"})
    @Severity(SeverityLevel.NORMAL)
    @Owner("JuanMartin")
    @Description("GET /users/search?q=John devuelve 3 usuarios relacionados")
    public void testSearchUsersJohn() {
        Response response = given()
            .queryParam("q", "John")
        .when()
            .get("/users/search")
        .then()
            .statusCode(200)
            .body("users", hasSize(3))
            .body("total", equalTo(3))
            .body("users[0].lastName",equalTo("Johnson"))
            .extract().response();
            // Puedes agregar más validaciones según quieras:
            // .body("users[0]", hasKey("firstName"))
            // .body("users[0]", hasKey("lastName"))

        // Adjuntar el JSON de la respuesta al reporte Allure
        Allure.addAttachment("JSON de la respuesta", "application/json", response.asPrettyString());    
    }

    @Test(description = "Obtener usuarios paginados (limit/skip)", groups = {"users"})
    @Severity(SeverityLevel.MINOR)
    @Owner("JuanMartin")
    @Description("GET /users?limit=5&skip=10 devuelve 5 usuarios, desde el #11")
    public void testGetUsersWithPagination() {
        int limit = 5, skip = 10;
        Response response = given()
            .queryParam("limit", limit)
            .queryParam("skip", skip)
            .when()
            .get("/users")
            .then()
            .statusCode(200)
            .body("users.size()", equalTo(limit))
            .body("skip", equalTo(skip))
            .body("limit", equalTo(limit))
            .extract().response();

        Allure.addAttachment("Usuarios paginados (JSON)", "application/json", response.asPrettyString());
    }

    @Test(description = "Crear un nuevo usuario", groups = {"users", "create"})
    @Severity(SeverityLevel.BLOCKER)
    @Owner("JuanMartin")
    @Description("POST /users/add crea un usuario y lo retorna con ID")
    public void testCreateUser() {
        String body = """
        {
            "firstName": "Test",
            "lastName": "User",
            "age": 33,
            "email": "test.user%d@dummyjson.com"
        }
        """.formatted(System.currentTimeMillis());

        Response response = given()
            .contentType("application/json")
            .body(body)
            .when()
            .post("/users/add")
            .then()
            .statusCode(201)
            .body("firstName", equalTo("Test"))
            .body("id", notNullValue())
            .extract().response();

        Allure.addAttachment("Usuario creado (JSON)", "application/json", response.asPrettyString());
    }

    @Test(description = "Actualizar datos de un usuario", groups = {"users", "update"})
    @Severity(SeverityLevel.NORMAL)
    @Owner("JuanMartin")
    @Description("PUT /users/{id} modifica los datos del usuario, deberia devolver 204 peor siempre devuelve 200")
    public void testUpdateUser() {
        int userId = 1;
        String body = """
        {
            "firstName": "JuanEditado",
            "lastName": "Martin"
        }
        """;

        Response response = given()
            .contentType("application/json")
            .body(body)
            .when()
            .put("/users/{id}", userId)
            .then()
            .statusCode(200)
            .body("firstName", equalTo("JuanEditado"))
            .body("id", equalTo(userId))
            .extract().response();

        Allure.addAttachment("Usuario actualizado (JSON)", "application/json", response.asPrettyString());
    }

    @Test(description = "Eliminar un usuario", groups = {"users", "delete"})
    @Severity(SeverityLevel.NORMAL)
    @Owner("JuanMartin")
    @Description("DELETE /users/{id} elimina el usuario y responde con el usuario eliminado")
    public void testDeleteUser() {
        int userId = 2;
        Response response = given()
            .when()
            .delete("/users/{id}", userId)
            .then()
            .statusCode(200)
            .body("id", equalTo(userId))
            .body("isDeleted", is(true))
            .extract().response();

        Allure.addAttachment("Usuario eliminado (JSON)", "application/json", response.asPrettyString());
    }
}