package ink.verge.checkin;

import cn.hutool.crypto.symmetric.SymmetricCrypto;
import ink.verge.checkin.mbg.mapper.UserMapper;
import ink.verge.checkin.mbg.model.User;
import ink.verge.checkin.mbg.model.UserExample;
import ink.verge.checkin.utils.YibanUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author Verge
 * @Date 2020/12/6 12:57
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RemoveInvalidUser {
    @Autowired
    private YibanUtils yibanUtils;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SymmetricCrypto aes;
    @Test
    public void remove(){
        List<User> userList = userMapper.selectByExample(new UserExample());
        for (User user : userList) {
            String password = aes.decryptStr(user.getPassword());
            if (!yibanUtils.verifyAccount(user.getAccount(),password)){
                userMapper.deleteByPrimaryKey(user.getUid());
                log.info("已删除: "+user.getAccount());
            }
        }
    }

}
