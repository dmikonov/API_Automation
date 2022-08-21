package contacts;

import com.google.gson.Gson;
import dto.AuthRequestdto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTests {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Test
    public void LoginSuccess() throws IOException {
        AuthRequestdto auth = AuthRequestdto.builder().email("d020797@gmail.com").password("Ww12345$").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/login")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
    }

    @Test
    public void LoginNegativeWrongEmailFormat() throws IOException {
        AuthRequestdto auth = AuthRequestdto.builder().email("d020797gmail.com").password("Ws12345$").build();
        RequestBody requestBody = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/login")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),400);
    }

}