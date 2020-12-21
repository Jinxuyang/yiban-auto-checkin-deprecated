package ink.verge.checkin.utils;

import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ink.verge.checkin.common.CommonResult;
import ink.verge.checkin.mbg.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class YibanUtils {

    @Autowired
    private SymmetricCrypto aes;

    private static Map<String,Object> morMap = new LinkedHashMap<>();
    private static Map<String,Object> noonMap = new LinkedHashMap<>();
    private static final String morUrl = "http://yiban.sust.edu.cn/v4/public/index.php/Index/formflow/add.html?desgin_id=24&list_id=12";
    private static final String noonUrl = "http://yiban.sust.edu.cn/v4/public/index.php/Index/formflow/add.html?desgin_id=25&list_id=12";
    static {
        morMap.put("24[0][0][name]","form[24][field_1588749561_2922][]");
        morMap.put("24[0][0][value]","36.5");

        morMap.put("24[0][1][name]","form[24][field_1588749738_1026][]");
        morMap.put("24[0][1][value]","陕西省+西安市+未央区+111县道+111县+靠近陕西科技大学学生生活区+");

        morMap.put("24[0][2][name]","form[24][field_1588749759_6865][]");
        morMap.put("24[0][2][value]","是");

        morMap.put("24[0][3][name]","form[24][field_1588749842_2715][]");
        morMap.put("24[0][3][value]","否");

        morMap.put("24[0][4][name]","form[24][field_1588749886_2103][]");
        morMap.put("24[0][4][value]","");

        noonMap.put("25[0][0][name]","form[25][field_1588750276_2934][]");
        noonMap.put("25[0][0][value]","36.5");

        noonMap.put("25[0][1][name]","form[25][field_1588750304_5363][]");
        noonMap.put("25[0][1][value]","陕西省+西安市+未央区+111县道+111县+靠近陕西科技大学学生生活区+");

        noonMap.put("25[0][2][name]","form[25][field_1588750323_2500][]");
        noonMap.put("25[0][2][value]","是");

        noonMap.put("25[0][3][name]","form[25][field_1588750343_3510][]");
        noonMap.put("25[0][3][value]","否");

        noonMap.put("25[0][4][name]","form[25][field_1588750363_5268][]");
        noonMap.put("25[0][4][value]","");
    }



    public String getAccessToken(String username, String password) throws BusinessException {
        log.info("-----------------------------------------------------------------------");
        log.info("PARAM: username "+ username);
        log.debug("PARAM: password "+ password);

        String accessToken = null;

        String dePassword = aes.decryptStr(password);

        Map<String,Object> map = new HashMap<>();
        map.put("mobile",username);
        map.put("password",dePassword);
        map.put("imei",IMEIUtils.getIMEI());


        String jsonStr = HttpUtil.get("https://mobile.yiban.cn/api/v3/passport/login",map);

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonStr);
        JsonObject root = element.getAsJsonObject();
        String code = root.getAsJsonPrimitive("response").getAsString();

        // 成功
        if (code.equals("100")) {
            JsonObject userObject = root.getAsJsonObject("data").getAsJsonObject("user");
            accessToken = userObject.getAsJsonPrimitive("access_token").getAsString();
            String nick = userObject.getAsJsonPrimitive("nick").getAsString();
            log.info("nick: "+nick);
        }else {
            String errMsg = root.getAsJsonPrimitive("message").getAsString();
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL,errMsg);
        }

        return accessToken;
    }

    public String getCookie(String accesstoken){
        log.debug("PARAM: access_token " + accesstoken);

        try {
            if (accesstoken == null) throw new Exception();
            HttpResponse response = HttpRequest
                    .get("http://f.yiban.cn/iapp/index?act=iapp610661&access_token="+accesstoken)
                    .setFollowRedirects(true)
                    .execute();
            String res = response.header("Set-Cookie");
            log.debug("SUCCESS: Cookie " + res);
            return res;
        } catch (Exception e) {
            log.info("Exception: 未获取到SESSID");
            e.printStackTrace();
            return null;
        }
    }

    public CommonResult submit(String cookie, int type){
        log.debug("PARAM: cookie " + cookie);
        log.info("PARAM: type " + type);

        double temp = (new Random().nextInt(4)+4)/10.0+36;
        Map<String,Object> map;
        String url;

        if (type == 1){
            map = morMap;
            map.replace("24[0][0][value]",temp);
            url = morUrl;
        } else {
            map = noonMap;
            map.replace("25[0][0][value]",temp);
            url = noonUrl;
        }


        try {
            if (cookie == null) throw new Exception();

            HttpResponse response = HttpRequest.post(url)
                    .contentType("application/x-www-form-urlencoded")
                    .form(map)
                    .cookie(cookie)
                    .execute();

            JsonParser parser =  new JsonParser();
            JsonElement element = parser.parse(response.body());
            JsonObject root = element.getAsJsonObject();
            String code = root.getAsJsonPrimitive("code").getAsString();
            String msg = root.getAsJsonPrimitive("msg").getAsString();

            if (code.equals("1") || msg.contains("请勿多次提交") || msg.contains("SU")){
                log.info("SUCCESS: "+msg);
                return CommonResult.success(msg);
            } else {
                log.info("FAILED: " + msg);
                return CommonResult.failed(msg);
            }
        } catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed("提交信息是发生异常");
        }

    }


    public boolean checkin(User user,int type) {
        log.debug("PARAM: user " + user);
        log.info("PARAM: type " + type);

        boolean flag = false;
        String message = "\n日期: "+ new Date();
        try {
            String accessToken = getAccessToken(user.getAccount(),user.getPassword());
            if (accessToken == null) throw new Exception();

            String cookie = getCookie(accessToken);
            if (cookie == null) throw new Exception();

            CommonResult info = submit(cookie,type);
            message += "\n易班返回消息"+info.getMessage();

            flag = info.getCode() == 200;
            return flag;
        }catch (Exception e){
            e.printStackTrace();
            log.error("签到失败");
            return false;
        }finally {
            String mail = user.getMail();
            if (mail != null) {
                try {
                    if (flag) MailUtil.send(mail,"今日打卡成功", message,false);
                    else MailUtil.send(mail,"！！！今日打卡失败！！！", message,false);
                } catch (Exception e){
                    log.info("邮件发送失败");
                }
            }

        }
    }

    public boolean verifyAccount(String username, String password){
        log.info("PARAM: username "+ username);
        log.debug("PARAM: password "+ password);

        Map<String,Object> map = new HashMap<>();
        map.put("mobile",username);
        map.put("password",password);
        map.put("imei",IMEIUtils.getIMEI());


        String jsonStr = HttpUtil.get("https://mobile.yiban.cn/api/v3/passport/login",map);

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonStr);
        JsonObject root = element.getAsJsonObject();
        String code = root.getAsJsonPrimitive("response").getAsString();

        // 成功
        return code.equals("100");
    }
}
