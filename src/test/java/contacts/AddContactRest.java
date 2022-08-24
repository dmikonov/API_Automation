package contacts;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AddContactRest {
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImQwMjA3OTdAZ21haWwuY29tIn0.Qj2BrliOA9jElViUrZC7PyPZngeeiDgfF6m2IC5dRYE";
    @Test
    public void AddNewContactSuccess(){

        int i = (int) (System.currentTimeMillis() / 1000) % 3600;
        ContactDto contactDto = ContactDto.builder()
                .name("John")
                .lastName("Wick")
                .email("wick@" + i + "mail.com")
                .address("NY")
                .description("BF")
                .phone("121212" + i)
                .build();
        ContactDto contact = given()
                .header("Authorization",token)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .post("contact")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .as(ContactDto.class);
        Assert.assertEquals(contactDto.getName(),contact.getName());
        Assert.assertEquals(contactDto.getPhone(),contact.getPhone());
        Assert.assertEquals(contactDto.getEmail(),contact.getEmail());
        Assert.assertEquals(contactDto.getLastName(),contact.getLastName());

        Assert.assertNotEquals(contactDto.getId(),contact.getId());
    }
    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contacts-telran.herokuapp.com/";
        RestAssured.basePath = "api";
    }
    @Test
    public void AddNewContactSuccess2() {

        int i = (int) (System.currentTimeMillis() / 1000) % 3600;
        ContactDto contactDto = ContactDto.builder()
                .name("John")
                .lastName("Wick")
                .email("wick@" + i + "mail.com")
                .address("NY")
                .description("BF")
                .phone("121212" + i)
                .build();
       int id = given()
                .header("Authorization", token)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .post("contact")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("id",is(not(equalTo(contactDto.getId()))))
                .extract().path("id");

        System.out.println("id = " + id);
    }
}
