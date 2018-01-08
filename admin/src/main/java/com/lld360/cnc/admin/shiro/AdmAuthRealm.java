package com.lld360.cnc.admin.shiro;

import com.lld360.cnc.model.Admin;
import com.lld360.cnc.service.AdminService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author: dhc
 * Date: 2016-07-13 14:06
 */
public class AdmAuthRealm extends AuthorizingRealm {
    @Autowired
    AdminService adminService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Admin admin = (Admin) principalCollection.getPrimaryPrincipal();
        if (admin.getAccount().equals("cncadmin")) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRole("admin");
            info.addStringPermission("admin");
            return info;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        Admin admin = adminService.get(token.getUsername());
        if (admin != null) {
            return new SimpleAuthenticationInfo(admin, admin.getPassword(), admin.getAccount());
        }
        return null;
    }
}
