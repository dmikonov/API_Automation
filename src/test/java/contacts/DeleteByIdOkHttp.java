package contacts;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ResponseDeleteById;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class DeleteByIdOkHttp {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImQwMjA3OTdAZ21haWwuY29tIn0.Qj2BrliOA9jElViUrZC7PyPZngeeiDgfF6m2IC5dRYE";
//5672 5678
int id;

    @BeforeMethod
    public void preCondition() throws IOException {
        ContactDto contactDto = ContactDto.builder()
                .name("John")
                .lastName("Wick")
                .email("wick@mail.com")
                .address("NY")
                .description("BF")
                .phone("121212")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/contact")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        ContactDto contact = gson.fromJson(response.body().string(), ContactDto.class);
        id = contact.getId();
    }
    @Test
    public void deleteByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/contact/"+id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        ResponseDeleteById responseDeleteById = gson.fromJson(response.body().string(), ResponseDeleteById.class);
        System.out.println(responseDeleteById.getStatus());
        Assert.assertEquals(responseDeleteById.getStatus(),"Contact was deleted!");
    }
}
