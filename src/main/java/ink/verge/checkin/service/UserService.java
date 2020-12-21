package ink.verge.checkin.service;

import ink.verge.checkin.mbg.model.User;

import java.util.List;

/**
 * @Author Verge
 * @Date 2020/9/26 20:54
 */

public interface UserService {
    /**
     * 新增用户
     * @return
     */
    int insertUser(User user);

    /**
     * 删除用户
     * @return
     */
    int deleteUser(int uid);

    /**
     * 修改用户信息
     * @return
     */
    int changeUserInfo(User user);

    /**
     * 获取未完成午间签到的人
     * @return 未完成午间签到的列表
     */
    List<User> getNoonUndoneUser();

    /**
     * 获取未完成晨间签到的人
     * @return 未完成晨间签到的列表
     */
    List<User> getMorUndoneUser();

    /**
     * 重置签到状态为false
     * @return
     */
    int resetCheckinStatus();

    /**
     * 设置签到状态为true
     * @param user
     * @param type
     * @return
     */
    int setCheckinStatus(User user, int type);

    /**
     * 获取用户ByID
     * @param uid
     * @return
     */
    User getUserByUID(int uid);

    /**
     * 通过手机号获取用户
     * @param account
     * @return
     */
    User getUserByAccount(String account);

    /**
     * 通过openid获取用户
     * @param openId
     * @return
     */
    User getUserByOpenId(String openId);

    /**
     * 校验用户名和密码是否正确
     * @param account
     * @param password
     * @return
     */
    boolean verifyAccount(String username,String password);

}
