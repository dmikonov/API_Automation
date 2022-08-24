package superscheduler;

import com.google.gson.Gson;
import dtosuper.PeriodDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class GetAllRecordsOkHttp {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImQwMjA3OTdAZ21haWwuY29tIn0.Qj2BrliOA9jElViUrZC7PyPZngeeiDgfF6m2IC5dRYE";

    @Test
    public void getAllRecordsSuccess() throws IOException {
        PeriodDto periodDto = PeriodDto.builder()
                .monthFrom(1)
                .monthTo(5)
                .yearFrom(2020)
                .yearTo(2022)
                .build();
        RequestBody body = RequestBody.create(gson.toJson(periodDto), JSON);
        Request request = new Request.Builder()
                .url("https://super-scheduler-app.herokuapp.com/api/records")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
    }
}
