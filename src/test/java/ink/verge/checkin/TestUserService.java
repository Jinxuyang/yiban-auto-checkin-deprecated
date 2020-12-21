package ink.verge.checkin;

import ink.verge.checkin.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author Verge
 * @Date 2020/12/6 20:26
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestUserService {
    @Autowired
    private UserService userService;
    @Test
    public void test(){
        System.out.println(userService.getUserByAccount("1231"));
    }
}
