package contacts;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImQwMjA3OTdAZ21haWwuY29tIn0.Qj2BrliOA9jElViUrZC7PyPZngeeiDgfF6m2IC5dRYE
public class LoginTestsOkHttp {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Test
    public void loginSuccess() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().email("d020797@gmail.com").password("Ww12345$").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/login")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        AuthResponseDto responseDto = gson.fromJson(response.body().string(), AuthResponseDto.class);
        System.out.println(responseDto.getToken());
    }

    @Test
    public void loginNegativeWrongEmailFormat() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().email("d020797gmail.com").password("Ws12345$").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/login")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getCode());
        System.out.println(errorDto.getMessage());

        Assert.assertEquals(errorDto.getCode(), 400);
        Assert.assertEquals(errorDto.getMessage(), "Wrong email format! Example: name@mail.com");
    }

    @Test
    public void loginWrongPasswordLess8Symbols() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().email("d020797@gmail.com").password("Wc12345").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/login")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getCode());
        System.out.println(errorDto.getMessage());

        Assert.assertEquals(errorDto.getCode(), 400);
        Assert.assertEquals(errorDto.getMessage(), "Password length need be 8 or more symbols");
        Assert.assertTrue(errorDto.getMessage().contains("Password"));
    }

    @Test
    public void loginWrongPasswordWithoutSpecialSymbol() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().email("d020797@gmail.com").password("Wc12345f").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/login")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getCode());
        System.out.println(errorDto.getMessage());

        Assert.assertEquals(errorDto.getCode(), 400);
        Assert.assertEquals(errorDto.getMessage(), "Password must contain at least one special symbol from ['$','~','-','_']!");
        Assert.assertTrue(errorDto.getMessage().contains("Password"));
    }
    @Test
    public void loginWrongUnregisterUser() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().email("d020796@gmail.com").password("Wc12345$").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/login")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getCode());
        System.out.println(errorDto.getMessage());
        Assert.assertEquals(errorDto.getMessage(), "Wrong email or password!");
        Assert.assertEquals(errorDto.getCode(), 401);
        Assert.assertEquals(response.code(), 401);

        // Assert.assertTrue(errorDto.getMessage().contains("Password"));
    }

}