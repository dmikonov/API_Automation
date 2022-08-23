package contacts;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
//wick@2121mail.com error 409
public class AddContactOkHttp {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImQwMjA3OTdAZ21haWwuY29tIn0.Qj2BrliOA9jElViUrZC7PyPZngeeiDgfF6m2IC5dRYE";

    @Test
    public void newContactSuccess() throws IOException {
        int i = (int) (System.currentTimeMillis() / 1000) % 3600;
        ContactDto contactDto = ContactDto.builder()
                .name("John")
                .lastName("Wick")
                .email("wick@" + i + "mail.com")
                .address("NY")
                .description("BF")
                .phone("121212" + i)
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
        System.out.println(contact.getId());
        System.out.println(contact.getEmail());
        //contactDto.equals(contact);
        Assert.assertEquals(contactDto.getName(),contact.getName());
        Assert.assertEquals(contactDto.getPhone(),contact.getPhone());
        Assert.assertEquals(contactDto.getEmail(),contact.getEmail());
        Assert.assertEquals(contactDto.getLastName(),contact.getLastName());

        Assert.assertNotEquals(contactDto.getId(),contact.getId());
    }

    @Test
    public void newContactWrongNameIsEmpty() throws IOException {
        int i = (int) (System.currentTimeMillis() / 1000) % 3600;
        ContactDto contactDto = ContactDto.builder()
                .lastName("Wick")
                .email("wick@" + i + "mail.com")
                .address("NY")
                .description("BF")
                .phone("121212" + i)
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/contact")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getMessage(),"Wrong contact format! Name can't be empty!");
    }

    @Test
    public void newContactWrongAuth() throws IOException {
        ContactDto contactDto = ContactDto.builder()
                .name("John")
                .lastName("Wick")
                .email("wick@mail.com")
                .address("NY")
                .description("BF")
                .phone("121212121212")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/contact")
                .post(body)
                .addHeader("Authorization", "gghfgd")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getMessage(),"Wrong token format!");
        Assert.assertEquals(response.code(), 401); //bug 500
    }

    @Test
    public void newContactDuplicate() throws IOException {
        ContactDto contactDto = ContactDto.builder()
                .name("John")
                .lastName("Wick")
                .email("wick@2121mail.com")
                .address("NY")
                .description("BF")
                .phone("121212121212")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/contact")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        //Assert.assertFalse(response.isSuccessful());

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getMessage(),"Wrong token format!");
        Assert.assertEquals(response.code(), 409); //bug 500
    }
}
