package ink.verge.checkin.service.impl;

import cn.hutool.http.HttpUtil;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ink.verge.checkin.controller.model.WxUserMessageModel;
import ink.verge.checkin.mbg.model.User;
import ink.verge.checkin.service.UserService;
import ink.verge.checkin.service.WxService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 微信服务类
 * @Author: lmwis
 * @Date 2020-11-28 15:52
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class WxServiceImpl implements WxService {

    @Value("${fehead.wx.appid}")
    static String appId;
    @Value("${fehead.wx.appsecret}")
    static String appSecret;
    private static final String ACCRESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    private static String accessToken;
    private static Map<String,Object> mapParam = new HashMap<>();

    private final UserService userService;

    private final String yibanCheckUrl = "易班自动打卡：\nhttp://47.93.200.138:8099";

    static {
        mapParam.put("grant_type","client_credential");
        mapParam.put("appid",appId);
        mapParam.put("secret",appSecret);
    }

    /**
     * 获取access_token
     * 一般一个token过期时间为7200秒，即2小时
     * 每日上线2000次
     */
    public void getAccessToken() throws BusinessException {
        String jsonStr = HttpUtil.get(ACCRESS_TOKEN_URL, mapParam);
        JsonParser parser = new JsonParser();
        JsonObject root = parser.parse(jsonStr).getAsJsonObject();
        boolean errcode = root.getAsJsonPrimitive("errcode").isJsonNull();
        String errMsg = "";
        // 成功
        if (!errcode) {
            accessToken = root.getAsJsonPrimitive("access_token").getAsString();
        }else {
            errMsg= root.getAsJsonPrimitive("errmsg").getAsString();
//            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL,errMsg);
        }
    }


    @Override
    public WxUserMessageModel dealWhenText(WxUserMessageModel userMessage) throws BusinessException {

        if(userMessage==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        WxUserMessageModel responseData = new WxUserMessageModel();
        packageCommonData(responseData,userMessage);
        responseData.setMsgType("text");

        if (StringUtils.equals(userMessage.getContent(), "打卡")) {
            responseData.setContent(yibanCheckUrl);
        } else if (StringUtils.equals(userMessage.getContent(), "打卡状态")) {
            responseData.setContent(checkCheckState(userMessage.getFromUserName()));
        } else if (StringUtils.startsWith(userMessage.getContent(), "手机号")) {
            responseData.setContent(registerUser(userMessage.getFromUserName(), userMessage.getContent()));
        } else {
            responseData.setContent(userMessage.getContent());
        }
        return responseData;
    }

    @Override
    public WxUserMessageModel dealWhenEvent(WxUserMessageModel userMessage) throws BusinessException {
        if(userMessage==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        WxUserMessageModel responseData = new WxUserMessageModel();
        packageCommonData(responseData,userMessage);
        if(StringUtils.equals(userMessage.getEvent(),"CLICK")&&StringUtils.equals(userMessage.getEventkey(),"V1_YIBAN_CHAEK_STATE")){
            responseData.setMsgType("text");
            String replayStr = checkCheckState(responseData.getToUserName());
            responseData.setContent(replayStr);
        }
        return responseData;
    }

    /**
     * 封装基本数据
     * @param responseData 返回数据
     * @param originData 源数据
     */
    private void packageCommonData(WxUserMessageModel responseData,WxUserMessageModel originData){
        responseData.setToUserName(originData.getFromUserName());
        responseData.setFromUserName(originData.getToUserName());
        responseData.setCreateTime(new Date().getTime());
    }

    private String registerUser(String fromUserName, String content) {
        String tel = StringUtils.substringAfter(content, "手机号");
        tel = tel.trim();
        User userByAccount = userService.getUserByAccount(tel);
        if (userByAccount == null) {
            return "绑定失败，请确认手机号是否正确或者格式问题，例：手机号15389159576";
        }
        userByAccount.setOpenid(fromUserName);
        userService.changeUserInfo(userByAccount);
        return "绑定成功";
    }

    /**
     * 根据openid查询用户打卡状态
     * @param openId
     * @return
     */
    private String checkCheckState(String openId) {
        User userByOpenId = userService.getUserByOpenId(openId);
        if (userByOpenId == null) {
            return "未绑定账号，请先登录";
        }
        Boolean morstatus = userByOpenId.getMorstatus();
        Boolean noonstatus = userByOpenId.getNoonstatus();
        String content = "今日打卡状态：\n";
        Calendar instance = Calendar.getInstance();
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        if (hour < 6) {
            content += "晨检：未开始 \n";
        } else {
            if (morstatus) {
                content += "晨检：成功\n";
            } else {
                content += "晨检：失败! 请手动打卡\n";
            }
        }
        if (hour < 12) {
            content += "午检：未开始";
        } else {
            if (noonstatus) {
                content += "午检：成功";
            } else {
                content += "午检：失败! 请手动打卡";
            }
        }
        return content;

    }

}
