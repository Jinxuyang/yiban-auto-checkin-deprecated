package ink.verge.checkin;

import cn.hutool.http.HttpRequest;
import org.junit.Test;

/**
 * @Author Verge
 * @Date 2020/11/24 13:17
 * @Version 1.0
 */

public class TestHttpUtils {


    @Test
    public void test (){
        String res = HttpRequest.get("http://localost:8401/testB")
                .timeout(2000)
                .execute().body();
        System.out.println(res);
    }
}
