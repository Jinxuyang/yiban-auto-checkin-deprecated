package ink.verge.checkin;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author Verge
 * @Date 2020/11/24 13:40
 * @Version 1.0
 */
public class TestOKHttp {
    OkHttpClient client = new OkHttpClient();
    @Test
    public void test(){
        Request request = new Request.Builder()
                .get()
                .url("https://mobile.yiban.cn/api/v3/passport/login?mobile=17765014581&imei=1&password=jinxyang123")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }
}

