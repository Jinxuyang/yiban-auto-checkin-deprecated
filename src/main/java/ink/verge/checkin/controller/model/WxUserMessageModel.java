package ink.verge.checkin.controller.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * @Description: 微信用户消息实体类
 * @Author: lmwis
 * @Date 2020-11-28 18:04
 * @Version 1.0
 */
@Data
@XStreamAlias("xml")
public class WxUserMessageModel {

    @XStreamAlias("ToUserName")
    private String toUserName;
    @XStreamAlias("FromUserName")
    private String fromUserName;

    @XStreamAlias("CreateTime")
    private Long createTime;

    @XStreamAlias("MsgType")
    private String msgType;

    @XStreamAlias("Content")
    private String content;

    @XStreamAlias("MsgId")
    private String msgId;

    @XStreamAlias("Title")
    private String title;

    @XStreamAlias("Description")
    private String description;

    @XStreamAlias("Url")
    private String url;

    /**
     * 订阅或者取消订阅的事件
     */
    @XStreamAlias("Event")
    private String event;

    @XStreamAlias("EventKey")
    private String eventkey;

    @XStreamAlias("MenuId")
    private String menuId;
}
