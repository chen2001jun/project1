package com.lld360.cnc.admin.controller;

import com.lld360.cnc.base.BaseController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.User;
import com.lld360.cnc.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "admin/user")
public class AdmUserController extends BaseController {

    @Autowired
    private UserService userService;

    //列表-分页
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<UserDto> usersGet(@RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortType) {
        Map<String, Object> params = getParamsPageMap(15);
        boolean sort = sortBy != null && sortType != null && sortBy.matches("total_score") && sortType.matches("asc|desc");
        if(!sort) {
            params.remove("sortBy");
            params.remove("sortType");
        }
        return userService.getUsersByPage(params);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public User userGet(@PathVariable long id) {
        return userService.getUser(id);
    }

    // 删除用户
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut userDelete(@PathVariable long id) {
        userService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

    // 上传用户头像
    @RequestMapping(value = "{id}/avatar", method = RequestMethod.POST)
    public User userAvatarPost(@PathVariable long id, MultipartFile file) {
        User user = userService.getUser(id);
        if (user != null) {
            return userService.setAvatar(user, file);
        }
        throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
    }

    //修改
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public User userPut(@PathVariable long id, @Valid @RequestBody User user) {
        user.setId(id);
        return userService.edit(user);
    }
}
