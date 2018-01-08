package com.lld360.cnc.core.bean;

import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.utils.JsonUtils;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-02-25 17:03
 */
public class AliSmsSender {
    public static final String SMS_TPL_VALIDCODE = "SMS_12990965";

    @Autowired
    private Configer configer;

    // 发送短信
    public String sendSms(String templateId, String mobile, Map<String, String> content) throws ApiException {
        String contentString = "";
        if (content != null && !content.isEmpty()) {
            contentString = JsonUtils.toJson(content);
        }
        return sendSms(templateId, mobile, contentString);
    }

    // 发送短信
    public String sendSms(String templateId, String mobile, String context) throws ApiException {
        if (StringUtils.isEmpty(context)) {
            return null;
        }
        TaobaoClient client = configer.getTaobaoSmsClient();
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("");
        req.setSmsType("normal");
        req.setSmsFreeSignName("大牛数控");
        req.setSmsParamString(context);
        req.setRecNum(mobile);
        req.setSmsTemplateCode(templateId);
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        return rsp.getBody();
    }
}
