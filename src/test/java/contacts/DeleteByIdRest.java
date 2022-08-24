package contacts;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import dto.ErrorDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class DeleteByIdRest {

    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImQwMjA3OTdAZ21haWwuY29tIn0.Qj2BrliOA9jElViUrZC7PyPZngeeiDgfF6m2IC5dRYE";
    int id;

    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contacts-telran.herokuapp.com/";
        RestAssured.basePath = "api";

        int i = (int) (System.currentTimeMillis() / 1000) % 3600;
        ContactDto contactDto = ContactDto.builder()
                .name("John")
                .lastName("Wick")
                .email("wick@" + i + "mail.com")
                .address("NY")
                .description("BF")
                .phone("121212" + i)
                .build();
        id = given()
                .header("Authorization", token)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .post("contact")
                .then()
                .assertThat().statusCode(200)
                .extract().path("id");
        System.out.println(id);
    }

    @Test
    public void deleteByIdSuccess() {
        given()
                .header("Authorization", token)
                .when()
                .delete("contact/" + id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("status", containsString("Contact was deleted!"));
    }

    @Test
    public void deleteByIdWrongAuth() {
        given()
                .header("Authorization", "ggg")
                .when()
                .delete("contact/" + id)
                .then()
                .assertThat().body("message", containsString("Wrong token format!"))
                .assertThat().statusCode(401); //bug code 500
    }

    @Test
    public void deleteWrongId(){
        ErrorDto errorDto = given()
                .header("Authorization", token)
                .when()
                .delete("contact/0")
                .then()
                .assertThat().body("message", containsString("not found!"))
                .assertThat().statusCode(404)
                .extract().as(ErrorDto.class);
        System.out.println(errorDto.getMessage());
    }
}
