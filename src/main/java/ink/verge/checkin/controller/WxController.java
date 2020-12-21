package ink.verge.checkin.controller;

import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.WxUserMessageReturnType;
import com.thoughtworks.xstream.XStream;
import ink.verge.checkin.controller.model.WxUserMessageModel;
import ink.verge.checkin.service.UserService;
import ink.verge.checkin.service.WxService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 接受微信发的消息
 * @Author: lmwis
 * @Date 2020-11-28 16:19
 * @Version 1.0
 */
@RestController
@RequestMapping("/wx")
@RequiredArgsConstructor
public class WxController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(WxController.class);
    @Value("${fehead.wx.serve.aeskey}")
    String aesKey;
    @Value("${fehead.wx.serve.token}")
    String token;

    final UserService userService;
    private final WxService wxService;

    /**
     * 验证消息来自微信
     *
     * @param request
     * @param response
     * @return
     * @throws BusinessException
     */
    @GetMapping("/user/message")
    public String replyWxServe(ServletRequest request, ServletResponse response) throws BusinessException {
        // 获取数据
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (!validateNull(signature, timestamp, nonce, echostr)) {
            // 为空不是微信后台
            throw new BusinessException(EmBusinessError.SERVICE_AUTHENTICATION_INVALID);
        }
        List<String> list = new ArrayList<>();
        // 配置上所填写的token
        list.add(token);
        list.add(timestamp);
        list.add(nonce);
        // 字典序排序
        Collections.sort(list);
        // 拼接
        StringBuilder sb = new StringBuilder();
        list.forEach(sb::append);
        // sha1加密
        String hashCode = DigestUtils.sha1DigestAsHex(sb.toString());
        logger.info("hashCode:" + hashCode);
        logger.info("signature:" + signature);
        // 比对
        if (StringUtils.equals(hashCode, signature)) {
            return echostr;
        } else {
            return "";
        }
    }

    /**
     * 接受用户信息并作出自动回复
     * @param request
     * @param response
     * @return
     * @throws BusinessException
     * @throws IOException
     */
    @PostMapping("/user/message")
    public String replyUserMessage(ServletRequest request, ServletResponse response) throws BusinessException, IOException {

        WxUserMessageModel wxUserMessageModel = resolveXmlData(request.getInputStream());
        System.out.println(wxUserMessageModel);
        if (!validateNull(wxUserMessageModel)) {
            return "success";
        }
        WxUserMessageModel responseXmlData = new WxUserMessageModel();
        String msgType = wxUserMessageModel.getMsgType();
        if(StringUtils.equals(msgType,"text")){
            responseXmlData = wxService.dealWhenText(wxUserMessageModel);
        }else if(StringUtils.equals(msgType,"event")){
            responseXmlData = wxService.dealWhenEvent(wxUserMessageModel);
        }
        XStream xstream = new XStream();
        xstream.processAnnotations(WxUserMessageModel.class);
        xstream.setClassLoader(WxUserMessageModel.class.getClassLoader());
        return xstream.toXML(responseXmlData);  //XStream的方法，直接将对象转换成 xml数据

    }


    /**
     * 解析xml数据
     *
     * @param in 输入流
     * @return 微信用户信息实体类
     * @throws IOException
     */
    private WxUserMessageModel resolveXmlData(InputStream in) throws IOException {
        String xmlData = IOUtils.toString(in);
        WxUserMessageModel wxXmlData = null;
        XStream xstream = new XStream();
        // 设置加载类的类加载器
        xstream.setClassLoader(WxUserMessageModel.class.getClassLoader());
        xstream.processAnnotations(WxUserMessageModel.class);
        xstream.alias("xml", WxUserMessageModel.class);
        wxXmlData = (WxUserMessageModel) xstream.fromXML(xmlData);
        return wxXmlData;
    }

    @GetMapping("/user/message/test")
    public WxUserMessageReturnType testNet() {
        return WxUserMessageReturnType.create("测试成功");
    }
    /**
     * 用户登录接口：https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxbd5d8bebb07b1e84&redirect_uri=http%3A%2F%2Frt3mb8.natappfree.cc%2Fwx%2Fuser%2Flogin&response_type=code&scope=snsapi_base#wechat_redirect
     */
    @GetMapping("/user/login")
    public String userLogin(String code){
        logger.info(code);
        return "登录成功";
    }

}
