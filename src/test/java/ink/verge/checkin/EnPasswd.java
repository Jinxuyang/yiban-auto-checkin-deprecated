package ink.verge.checkin;

import cn.hutool.crypto.symmetric.SymmetricCrypto;
import ink.verge.checkin.mbg.mapper.UserMapper;
import ink.verge.checkin.mbg.model.User;
import ink.verge.checkin.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author Verge
 * @Date 2020/11/12 20:42
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EnPasswd {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SymmetricCrypto aes;
    @Test
    public void en (){
        List<User> list = userService.getMorUndoneUser();
        for (User user : list) {
            user.setPassword(aes.encryptBase64(user.getPassword()));
            userMapper.updateByPrimaryKey(user);
        }
    }
 }
