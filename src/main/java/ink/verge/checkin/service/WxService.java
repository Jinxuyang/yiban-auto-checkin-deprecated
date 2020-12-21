package ink.verge.checkin.service;

import com.fehead.lang.error.BusinessException;
import ink.verge.checkin.controller.model.WxUserMessageModel;

/**
 * @Description:
 * @Author: lmwis
 * @Date 2020-11-29 14:10
 * @Version 1.0
 */
public interface WxService {

    /**
     * 处理text类型
     * @return
     */
    public WxUserMessageModel dealWhenText(WxUserMessageModel userMessage) throws BusinessException;

    /**
     * 处理事件类型
     * @return
     */
    public WxUserMessageModel dealWhenEvent(WxUserMessageModel userMessage) throws BusinessException;
}
