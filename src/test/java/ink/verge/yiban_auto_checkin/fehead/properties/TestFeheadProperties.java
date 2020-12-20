package ink.verge.yiban_auto_checkin.fehead.properties;

import com.fehead.lang.properties.FeheadProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description:
 * @Author: lmwis
 * @Date 2020-12-20 11:52
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFeheadProperties {
    @Autowired
    FeheadProperties feheadProperties;

    /**
     * 配置注入测试
     * success
     */
    @Test
    public void TestPropertiesAutowired(){
        System.out.println(feheadProperties.getSmsProperties().getSmsModel());
    }
}
