package ink.verge.checkin.controller;

import cn.hutool.crypto.symmetric.SymmetricCrypto;
import ink.verge.checkin.common.CommonResult;
import ink.verge.checkin.mbg.model.User;
import ink.verge.checkin.run.RunCheckin;
import ink.verge.checkin.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RunCheckin runCheckin;
    @Autowired
    private SymmetricCrypto aes;

    @PostMapping(path = "/insert")
    public CommonResult addUser(@RequestParam String account,String password,String mail){
        log.info("PARAM: account " + account);
        log.debug("PARAM: password " + password);
        log.info("PARAM: account " + mail);

        String enPassword = aes.encryptBase64(password);

        User user = new User();
        user.setAccount(account);
        user.setPassword(enPassword);
        user.setMail(mail);

        if(userService.getUserByAccount(account) == null){
            if (userService.verifyAccount(account,password)){
                userService.insertUser(user);
                log.info("SUCCESS: 插入成功");
                return CommonResult.success("添加成功，下次打卡自动打");
            } else {
                log.error("FAIL: 账号或密码错误，插入失败");
                return CommonResult.failed("账号或密码错误，请检查");
            }
        } else {
            log.error("FAIL: 用户已存在，插入失败");
            return CommonResult.failed("用户已存在");
        }

    }

    @PutMapping("/{uid}")
    public CommonResult changeUserInfo(@PathVariable int uid , @RequestParam String address){
        log.info("PARAM: uid " + uid);
        log.info("PARAM: address " + address);

        User user = new User();

        user.setUid(uid);
        user.setAddress(address);

        if (userService.changeUserInfo(user) == 1) {
            log.info("SUCCESS: 修改成功");
            return CommonResult.success();
        } else {
            log.info("FAILED: 删除失败");
            return CommonResult.failed();
        }
    }
    /* @DeleteMapping("/{uid}")
    public CommonResult deleteUser(@PathVariable int uid){
        log.info("PARAM: uid " + uid );
        if (userService.deleteUser(uid) == 1){
            log.info("SUCCESS: 删除成功");
            return CommonResult.success();
        } else {
            log.info("FAILED: 删除失败");
            return CommonResult.failed();
        }
    }*/

    /*@GetMapping("/selectByAccount")
    public CommonResult getUser(String account){
        log.info("PARAM: account " + account);
        return CommonResult.success(null,userService.getUserByAccount(account));
    }*/

    @GetMapping("/checkin/{type}")
    public CommonResult checkin(@PathVariable int type) throws InterruptedException{
        log.info("PARAM: type " + type);
        if (type == 1){
            runCheckin.morCheck();
        } else {
            runCheckin.noonCheck();
        }
        return CommonResult.success();
    }
}
