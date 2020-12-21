package ink.verge.checkin;

import cn.hutool.extra.mail.MailUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author Verge
 * @Date 2020/11/12 17:46
 * @Version 1.0
 */
public class TestMail {
    @Test
    public void mailSend() {
        MailUtil.send("981340404@qq.com","test","test",false);
    }
}
