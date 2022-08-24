package contacts;

import com.jayway.restassured.RestAssured;
import dto.ContactDto;
import dto.GetAllContactsDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class GelAllContactsRest {
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImQwMjA3OTdAZ21haWwuY29tIn0.Qj2BrliOA9jElViUrZC7PyPZngeeiDgfF6m2IC5dRYE";


    @BeforeMethod
    public void preCondition() {
        RestAssured.baseURI = "https://contacts-telran.herokuapp.com/";
        RestAssured.basePath = "api";
    }
    @Test
    public void getAllContacts(){
        GetAllContactsDto list =   given()
                .header("Authorization",token)
                .when()
                .get("contact")
                .then()
                .assertThat().statusCode(200)
                .extract().as(GetAllContactsDto.class);
            // .extract().path("contacts");
        for(ContactDto dto : list.getContacts()){
            System.out.println(dto.toString());
            System.out.println("**************");
        }
    }
}
