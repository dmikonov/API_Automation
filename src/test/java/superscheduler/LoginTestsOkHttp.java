package superscheduler;

import com.google.gson.Gson;
import dtosuper.AuthRequestDto;
import dtosuper.AuthResponseDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTestsOkHttp {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    @Test
    public void loginSuccess() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().email("d020797@gmail.com").password("Ww12345$").build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://super-scheduler-app.herokuapp.com/api/login")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        AuthResponseDto responseDto = gson.fromJson(response.body().string(), AuthResponseDto.class);
        System.out.println(responseDto.getToken());
    }
        @Test
        public void loginInvalidPassword() throws IOException {
            AuthRequestDto auth = AuthRequestDto.builder().email("d020797@gmail.com").password("Ww12355$").build();
            RequestBody body = RequestBody.create(gson.toJson(auth),JSON);
            Request request = new Request.Builder()
                    .url("https://super-scheduler-app.herokuapp.com/api/login")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            Assert.assertFalse(response.isSuccessful());
            Assert.assertEquals(response.code(),401);
    }

    @Test
    public void loginInvalidEmail() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder().email("d020797gmail.com").password("Ww12345$").build();
        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);
        Request request = new Request.Builder()
                .url("https://super-scheduler-app.herokuapp.com/api/login")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        //Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401); //200 bug
    }
}
