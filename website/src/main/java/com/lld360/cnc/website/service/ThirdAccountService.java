package com.lld360.cnc.website.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.ThirdAccount;
import com.lld360.cnc.model.User;
import com.lld360.cnc.model.UserScore;
import com.lld360.cnc.repository.ThirdAccountDao;
import com.lld360.cnc.repository.UserScoreDao;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.website.dto.QqAccountAccessToken;
import com.lld360.cnc.website.dto.WxAccountAccessToken;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;

@Service
public class ThirdAccountService extends BaseService {
    Logger logger = LoggerFactory.getLogger(ThirdAccountService.class);

    @Autowired
    Configer configer;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ThirdAccountDao thirdAccountDao;

    @Autowired
    UserService userService;

    @Autowired
    UserScoreDao userScoreDao;


    public ThirdAccount create(ThirdAccount thirdAccount) {
        thirdAccountDao.create(thirdAccount);
        return thirdAccount;
    }

    public ThirdAccount update(ThirdAccount thirdAccount) {
        thirdAccountDao.update(thirdAccount);
        return thirdAccount;
    }

    public boolean findByOpenidCount(String openid) {
        return thirdAccountDao.findByOpenidCount(openid) > 0;
    }

    public ThirdAccount findByOpenid(String openid) {
        return thirdAccountDao.findByOpenid(openid);
    }

    public WxAccountAccessToken getWxAccessToken(String code) {
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                configer.getWxAccountAppid(), configer.getWxAccountScrect(), code);

        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        WxAccountAccessToken token = null;
        try {
            token = objectMapper.readValue(json, WxAccountAccessToken.class);
            if (token.getErrcode() != null) {
                logger.warn("获取微信登录AccessToken失败：%d --> %s", token.getErrcode(), token.getErrmsg());
            }
        } catch (IOException e) {
            logger.warn("转换微信登录AccessToken异常", e);
        }
        return token;
    }

    public ThirdAccount getWxUserinfo(WxAccountAccessToken token) {
        ThirdAccount thirdAccount = new ThirdAccount();
        String url = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s",
                token.getAccessToken(), token.getOpenid());

        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        try {
            JSONObject jsonObject = new JSONObject(new String(json.getBytes("ISO-8859-1"), "UTF-8"));
            String gender = String.valueOf(jsonObject.getInt("sex") == 1 ? (Const.Third_ACCOUNT_GENDER_MAN) : Const.Third_ACCOUNT_GENDER_WOMAN);
            jsonObject.put("icon", jsonObject.getString("headimgurl"));
            jsonObject.put("gender", gender);
            jsonObject.remove("sex");
            jsonObject.remove("country");
            jsonObject.remove("headimgurl");
            jsonObject.remove("language");
            jsonObject.remove("privilege");
            json = jsonObject.toString();
            ObjectMapper mapper = new ObjectMapper();
            thirdAccount = mapper.readValue(json, ThirdAccount.class);
            if (jsonObject.has("errcode") && jsonObject.getInt("errcode") != 0) {
                logger.warn("获取微信用户信息失败：%d --> %s", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        } catch (IOException e) {
            logger.warn("转换微信用户异常", e);
        }
        thirdAccount.setType(Const.Third_ACCOUNT_TYPE_WEIXIN);
        thirdAccount.setCreateTime(new Date());
        thirdAccount.setAccessToken(token.getAccessToken());
        thirdAccount.setExpiresIn(token.getExpiresIn());
        thirdAccount.setRefreshToken(token.getRefreshToken());
        return thirdAccount;
    }

    public QqAccountAccessToken getQqAccessToken(String code) {
        String url = String.format("https://graph.qq.com/oauth2.0/token?client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code&redirect_uri=%s",
                configer.getQqAccountAppid(), configer.getQqAccountScrect(), code, configer.getAppUrl());

        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        QqAccountAccessToken token = new QqAccountAccessToken();
        if (json.contains("error")) {
            logger.warn("获取QQ登录AccessToken失败:" + json);
            return null;
        } else {
            String[] jsonArray = json.split("&");
            for (String s : jsonArray
                    ) {
                if (s.startsWith("access_token=")) token.setAccessToken(s.substring(s.indexOf("=") + 1, s.length()));
                if (s.startsWith("expires_in="))
                    token.setExpiresIn(Integer.valueOf(s.substring(s.indexOf("=") + 1, s.length())));
                if (s.startsWith("refresh_token=")) token.setRefreshToken(s.substring(s.indexOf("=") + 1, s.length()));
            }
        }
        return token;
    }


    public QqAccountAccessToken getQqOpenId(QqAccountAccessToken token) {
        String url = String.format("https://graph.qq.com/oauth2.0/me?access_token=%s",
                token.getAccessToken());
        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        try {
            json = json.replace("callback(", "").replace(");\n", "").trim();
            JSONObject jsonObject = new JSONObject(new String(json.getBytes("ISO-8859-1"), "UTF-8"));
            token.setOpenid(jsonObject.getString("openid"));
            if (token.getErrcode() != null) {
                logger.warn("获取QQ的openid信息失败：%d --> %s", token.getErrcode(), token.getErrmsg());
            }
        } catch (IOException e) {
            logger.warn("转换QQ的openid异常", e);
        }
        return token;
    }

    public ThirdAccount getQqUserinfo(QqAccountAccessToken token) {
        ThirdAccount thirdAccount = new ThirdAccount();
        String url = String.format("https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s",
                token.getAccessToken(), configer.getQqAccountAppid(), token.getOpenid());

        RestTemplate template = new RestTemplate();
        String json = template.getForObject(url, String.class);
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.getInt("ret") != 0) {
                logger.warn("获取QQ用户信息失败：%d --> %s", jsonObject.getInt("ret"), jsonObject.getString("msg"));
                return null;
            }
            String gender = String.valueOf(jsonObject.getString("gender").equals("男") ? (Const.Third_ACCOUNT_GENDER_MAN) : Const.Third_ACCOUNT_GENDER_WOMAN);
            String icon = StringUtils.isNotEmpty(jsonObject.getString("figureurl_qq_2")) ? jsonObject.getString("figureurl_qq_2") : jsonObject.getString("figureurl_qq_1");
            jsonObject.put("icon", icon);
            jsonObject.put("gender", gender);
            jsonObject.put("nickname", jsonObject.getString("nickname"));
            String[] removeStrings = new String[]{"level", "year", "ret", "msg", "figureurl", "figureurl_1", "figureurl_2", "figureurl_qq_1", "figureurl_qq_2", "is_lost", "is_yellow_vip", "is_yellow_year_vip", "yellow_vip_level", "vip"};
            for (String removeString : removeStrings
                    ) {
                jsonObject.remove(removeString);
            }
            json = jsonObject.toString();
            ObjectMapper mapper = new ObjectMapper();
            thirdAccount = mapper.readValue(json, ThirdAccount.class);
        } catch (IOException e) {
            logger.warn("转换QQ用户异常", e);
        }
        thirdAccount.setOpenid(token.getOpenid());
        thirdAccount.setType(Const.Third_ACCOUNT_TYPE_QQ);
        thirdAccount.setCreateTime(new Date());
        thirdAccount.setAccessToken(token.getAccessToken());
        thirdAccount.setExpiresIn(token.getExpiresIn());
        thirdAccount.setRefreshToken(token.getRefreshToken());
        return thirdAccount;
    }


    public UserDto thirdAccountLogin(ThirdAccount thirdAccount) {
        UserDto user = new UserDto();
        user.setNickname(thirdAccount.getNickname());
        user.setAvatar(thirdAccount.getIcon());
        user.setCreateTime(thirdAccount.getCreateTime());
        user.setLastLogin(thirdAccount.getUpdateTime());
        user.setAddress(thirdAccount.getProvince() + "  " + thirdAccount.getCity());
        user.setState(Const.USER_STATE_NORMAL);
        user.setLoginType(thirdAccount.getType());
        user.setIsBind(thirdAccount.getUserId() != null);
        user.setOpenid(thirdAccount.getOpenid());
        user.setId(thirdAccount.getUserId());
        return user;
    }

    public void isBindThirdAccount(String mobile, byte type) {
        User user = userService.getByMobile(mobile);
        String accountType = "";
        if (type == Const.Third_ACCOUNT_TYPE_QQ) accountType = "QQ";
        if (type == Const.Third_ACCOUNT_TYPE_WEIXIN) accountType = "微信";
        if (user != null && thirdAccountDao.isBindThirdAccount(user.getId(), type) > 0)
            throw new ServerException(HttpStatus.BAD_REQUEST, "已有" + accountType + "账号绑定该手机！");
    }

    @Transactional
    public UserDto bindThirdAccount(String openid, String mobile, String password) {
        UserDto userDto = userService.getByMobile(mobile);
        userService.validateUserState(userDto);
        if (userDto == null) {
            ThirdAccount thirdAccount = thirdAccountDao.findByOpenid(openid);
            userDto=new UserDto();
            userDto.setName(mobile);
            userDto.setMobile(mobile);
            userDto.setPassword(DigestUtils.md5Hex(StringUtils.reverse(password) + mobile));
            userDto.setNickname(thirdAccount.getNickname());
            userDto.setCreateTime(thirdAccount.getCreateTime());
            userDto.setLastLogin(thirdAccount.getUpdateTime() == null ? thirdAccount.getCreateTime() : thirdAccount.getUpdateTime());
            userDto.setAvatar(thirdAccount.getIcon());
            userDto.setAddress(thirdAccount.getProvince() + "  " + thirdAccount.getCity());
            userDto.setState(Const.USER_STATE_NORMAL);
            userService.create(userDto);
            userScoreDao.create(new UserScore(userDto.getId(), 100, 100, 0));
            thirdAccount.setUserId(userDto.getId());
            thirdAccountDao.update(thirdAccount);
        } else {
            ThirdAccount thirdAccount = thirdAccountDao.findByOpenid(openid);
            userDto.setPassword(DigestUtils.md5Hex(StringUtils.reverse(password) + mobile));
            if (userDto.getName() == null) userDto.setName(mobile);
            if (userDto.getNickname() == null) userDto.setNickname(thirdAccount.getNickname());
            if (userDto.getLastLogin() == null)
                userDto.setLastLogin(thirdAccount.getUpdateTime() == null ? thirdAccount.getCreateTime() : thirdAccount.getUpdateTime());
            if (userDto.getAvatar() == null) userDto.setAvatar(thirdAccount.getIcon());
            if (userDto.getAddress() == null)
                userDto.setAddress(thirdAccount.getProvince() + "  " + thirdAccount.getCity());
            userService.updateUser(userDto);
            thirdAccount.setUserId(userDto.getId());
            thirdAccountDao.update(thirdAccount);
        }

        UserDto sessionUserdto = (UserDto) request.getSession().getAttribute(Const.SS_USER);
        sessionUserdto.setIsBind(true);
        sessionUserdto.setId(userDto.getId());
        request.getSession().setAttribute(Const.SS_USER, sessionUserdto);
        return userDto;
    }
}
