package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.bean.AliSmsSender;
import com.lld360.cnc.model.ValidSms;
import com.lld360.cnc.repository.ValidSmsDao;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ValidSmsService extends BaseService {
    private Logger logger = LoggerFactory.getLogger(ValidSmsService.class);

    @Autowired
    private ValidSmsDao validSmsDao;

    @Autowired
    private AliSmsSender aliSmsSender;

    @Autowired
    private Configer configer;

    public ValidSms get(String mobile, Byte type) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("type", type);
        return validSmsDao.find(params);
    }

    // 创建或更新短信验证码数据
    public ValidSms couSms(String mobile, Byte type) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("type", type);
        ValidSms sms = validSmsDao.find(params);
        Date now = new Date();
        long expiredTime;
        expiredTime = configer.getExpiredTime();
        if (sms != null) {
            if (sms.getExpiredTime().getTime() < now.getTime()) {
                sms.setValidCode(RandomStringUtils.randomNumeric(6));
            }
            sms.setExpiredTime(new Date(now.getTime() + expiredTime*60000));
            validSmsDao.update(sms);
        } else {
            sms = new ValidSms();
            sms.setMobile(mobile);
            sms.setType(type);
            sms.setExpiredTime(new Date(now.getTime() + expiredTime*60000));
            sms.setValidCode(RandomStringUtils.randomNumeric(6));
            validSmsDao.create(sms);
        }

        logger.info(MessageFormat.format("发送短信 --> 手机号:{0}, 验证码:{1}, 类型:{2}", mobile, sms.getValidCode(), sms.getType()));
        if (configer.getEnv().equals("prod")) {
            try {
                Map<String, String> content = new HashMap<>();
                content.put("code", sms.getValidCode());
                content.put("expired", String.valueOf(configer.getExpiredTime()));
                logger.info("发送短信结果：" + aliSmsSender.sendSms(AliSmsSender.SMS_TPL_VALIDCODE, mobile, content));
            } catch (ApiException e) {
                logger.warn(getMessage("E10008"), e);
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10008).setData(e.getMessage());
            }
        }

        return sms;
    }

    // 验证短信
    public void validSmsCode(String mobile, byte type, String code) {
        validSmsCode(mobile, type, code, true);
    }

    public void validSmsCode(String mobile, byte type, String code, boolean autoDelete) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("type", type);
        ValidSms sms = validSmsDao.find(params);
        Date now = new Date();
        if (sms == null || sms.getExpiredTime().getTime() < now.getTime()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10009);
        }
        if (!sms.getValidCode().equals(code)) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10010);
        }
        // 验证成功删除短信验证码
        if (autoDelete)
            validSmsDao.delete(params);
    }
}
