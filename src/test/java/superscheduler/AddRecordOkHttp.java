package superscheduler;

import com.google.gson.Gson;
import dtosuper.DateDto;
import dtosuper.RecordDto;
import dtosuper.ResponseAddRecordDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class AddRecordOkHttp {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImQwMjA3OTdAZ21haWwuY29tIn0.Qj2BrliOA9jElViUrZC7PyPZngeeiDgfF6m2IC5dRYE";

    @Test
    public void addRecordSuccess() throws IOException {
        RecordDto recordDto = RecordDto.builder()
                .breaks(2)
                .currency("Currency")
                .date(DateDto.builder()
                        .dayOfMonth(1)
                        .dayOfWeek("2")
                        .month(1)
                        .year(2020)
                        .build())
                .hours(4)
                .timeFrom("18:45")
                .timeTo("21:00")
                .title("title")
                .type("type")
                .totalSalary(500)
                .wage(50)
                .build();
        RequestBody body = RequestBody.create(gson.toJson(recordDto), JSON);
        Request request = new Request.Builder()
                .url("https://super-scheduler-app.herokuapp.com/api/record")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        ResponseAddRecordDto record = gson.fromJson(response.body().string(), ResponseAddRecordDto.class);
        System.out.println(record.getId());
    }
}
