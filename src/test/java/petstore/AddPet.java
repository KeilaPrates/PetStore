package petstore;


import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Validando a adição de Pet")
public class AddPet {

        @BeforeEach
        public void beforeEach() {
                baseURI = "https://petstore.swagger.io/v2/pet";

        }
        public String lerJson(String caminhoJson) throws IOException {
                return new String(Files.readAllBytes(Paths.get(caminhoJson)));
        }

        @Test
        @DisplayName("Inserindo pet")
        public void incluirPet() throws IOException {
                String jsonBody = lerJson("db/pet1.json");

                given()
                        .contentType("application/json")
                        .log().all()
                        .body(jsonBody)
                .when()
                        .post(baseURI)
                .then()
                        .log().all()
                        .statusCode(200)
                        .body("name", is("Nino"))
                        //.body("category.name", is("AX1279845BA"))
                        .body("tags.name", contains("data"))
                        .body("status", is("available"));

        }



        @Test
        @DisplayName("Consultar pet")
        public void consultarPet() throws IOException {
                String petId = "9223372036854023000";

                String token = given()
                        .contentType("application/json")
                        .log().all()
                .when()
                        .get(baseURI + "/" + petId)
                 .then()
                        .log().all()
                        .statusCode(200)
                        .body("name", is("Nino"))
                        .body("category.name", is("AX1279845BA"))
                        .body("status", is("available"))
                        .extract()
                        .path("data.token");

        }

        @DisplayName("Fazer alteração de Pet")
        @Test
        public void alterarPet() throws IOException {
                String jsonBody = lerJson("db/pet2.json");

                given()
                        .contentType("application/Json")
                        .log().all()
                        .body(jsonBody)
                .when()
                        .put(baseURI)
                .then()
                        .log().all()
                        .statusCode(200)
                        .body("name", is("Nino"))
                        .body("status", is("sold"));
        }

        @Test
        @DisplayName("Fazer a exclusão do pet")
        public void excluirPet(){
                String petId = "9223372036854023000";

                given()
                        .contentType("application/json")
                        .log().all()
                .when()
                        .delete(baseURI + "/" + petId)
                .then()
                        .log().all()
                        .statusCode(200)
                        .body("code", is(200))
                        .body("type", is("unknown"))
                        .body("message", is(petId));

        }

}




