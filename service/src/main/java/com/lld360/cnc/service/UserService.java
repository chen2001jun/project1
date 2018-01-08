package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.bean.AliSmsSender;
import com.lld360.cnc.core.utils.ClientUtils;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.User;
import com.lld360.cnc.model.UserScore;
import com.lld360.cnc.model.UserScoreHistory;
import com.lld360.cnc.repository.UserDao;
import com.lld360.cnc.repository.UserScoreDao;
import com.lld360.cnc.repository.UserScoreHistoryDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService {

    @Autowired
    UserDao userDao;

    @Autowired
    Configer configer;

    @Autowired
    ValidSmsService validSmsService;

    @Autowired
    UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    UserScoreDao userScoreDao;

    @Autowired
    AliSmsSender aliSmsSender;

    public void create(UserDto userDto) {
        userDao.create(userDto);
    }

    //列表-分页
    public Page<UserDto> getUsersByPage(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = userDao.count(params);
        List<UserDto> users = count > 0 ? userDao.search(params) : new ArrayList<>();
        return new PageImpl<>(users, pageable, count);
    }

    // 获取个人资料
    public User getUser(long id) {
        return userDao.find(id);
    }

    //TODO 获取用户数量 暂时这样 待修改
    public long getCount(Map<String, Object> param) {
        return userDao.count(param);
    }

    //手机号码注册
    @Transactional
    public UserDto registerWithMobile(String mobile, String password, String validCode) {
        if (StringUtils.isEmpty(mobile) || !mobile.matches("1\\d{10}")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10001);
        }
        if (StringUtils.isEmpty(password) || !password.matches("\\w{6,20}")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10002);
        }
        if (StringUtils.isEmpty(validCode) || !validCode.matches("\\d{6}")) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10003);
        }
        validSmsService.validSmsCode(mobile, Const.SMS_REGIST, validCode);

        UserDto user = new UserDto();
        user.setName(mobile);
        user.setMobile(mobile);
        user.setPassword(DigestUtils.md5Hex(StringUtils.reverse(password) + mobile));
        user.setCreateTime(new Date());
        user.setState(Const.USER_STATE_NORMAL);
        userDao.create(user);
        userScoreDao.create(new UserScore(user.getId(), 100, 100, 0));
        userScoreHistoryDao.create(new UserScoreHistory(user.getId(), Const.USER_SCORE_HISTORY_TYPE_REGIEST, 100, user.getId()));
        return user;
    }


    // 用户登录
    public User doLogin(String username, String password, HttpServletResponse response) {
        UserDto user = getByMobile(username);
        if (user == null || !user.getPassword().equals(DigestUtils.md5Hex(StringUtils.reverse(password) + username))) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10012);
        }
        validateUserState(user);
        request.getSession().setAttribute(Const.SS_USER, user);
        Cookie cookie = new Cookie("rememberMe", StringUtils.reverse(user.getPassword()) + '/' + Long.toString(user.getId() + 10000, Character.MAX_RADIX));
        cookie.setPath("/");
        cookie.setMaxAge(Const.REMEMBER_ME_TIME);
        response.addCookie(cookie);
        return user;
    }

    // 用户登出
    public void doLogout(HttpServletResponse response) {
        if (!request.getSession().isNew())
            request.getSession().invalidate();
        Cookie cookie = ClientUtils.getCookie(request, "rememberMe");
        if (cookie != null) {
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    // 删除用户
    public void delete(long id) {
        User user = userDao.find(id);
        if (user == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404).setMessage("用户不存在");
        } else {
            userDao.updateState(user.getId(), Const.USER_STATE_DELETED);
        }
    }

    // 编辑用户资料
    public User edit(User user) {
        if (user.getPassword() != null) {
            user.setPassword(DigestUtils.md5Hex(StringUtils.reverse(user.getPassword()) + user.getMobile()));
        }
//        user.setAvatar(null);
        user.setCreateTime(null);
        return userDao.update(user) > 0 ? getUser(user.getId()) : user;
    }

    public void updateByMobile(String mobile, String pwd) {
        User user = new User();
        user.setPassword(DigestUtils.md5Hex(StringUtils.reverse(pwd) + mobile));
        user.setMobile(mobile);
        userDao.updateByMobile(user);
    }

    //更新用户资料 yangb:可修改
    public boolean updateUser(User user) {
        return userDao.update(user) == 1;
    }

    public UserDto getByMobile(String loginName) {
        return userDao.findByMobile(loginName);
    }

    // 上传用户头像
    public User setAvatar(User user, MultipartFile file) {
        String relativePath = "user/" + user.getId() + "/";
        String relativeFile = relativePath + "avatar." + FilenameUtils.getExtension(file.getOriginalFilename());
        String absoluteFile = configer.getFileBasePath() + relativeFile;
        if (user.getAvatar() != null) {
            File oldAvatarFile = new File(configer.getFileBasePath() + user.getAvatar());
            if (oldAvatarFile.exists())
                FileUtils.deleteQuietly(oldAvatarFile);
        }
        try {
            File f = new File(absoluteFile);
            if (f.getParentFile().exists() || f.getParentFile().mkdirs())
                file.transferTo(f);
            userDao.updateAvatar(user.getId(), relativeFile);
            user.setAvatar(relativeFile);
            return user;
        } catch (IOException e) {
            logger.warn("保存用户头像失败");
            throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E90003).setData(absoluteFile);
        }
    }

    // 获取用户详细信息
    public UserDto getUserDto(long id) {
        return userDao.findUserDto(id);
    }

    //获取用户积分信息
    public UserScore findUserScore(long id) {
        return userScoreDao.find(id);
    }

    public void validateUserState(UserDto userDto) {
        byte state = userDto.getState();
        switch (state) {
            case Const.USER_STATE_DELETED:
                throw new ServerException(HttpStatus.BAD_REQUEST, M.E10018);
        }
    }
}
