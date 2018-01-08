<div class="sd leftside">
    <!--
    <div class="menu-header"><i class="fa fa-navicon"></i> 菜单</div>

    <div class="curtain" style="background-image: url(../images/curtain_clock.png);">
        <div class="curtain-content">
            <div class="clock"></div>
            <div class="date"></div>
        </div>
    </div>
    -->
    <div class="sd leftside">
        <div class="menu-header"><i class="fa fa-navicon"></i> 菜单</div>
        <div class="profile clearfix">
            <div class="profile-top">
                <div class="profile-info clearfix">
                    <div class="profile-avatar">
                        <img src="${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}">
                    </div>
                    <div class="profile-meta">
                        <div class="profile-name">${bt:out(sessionScope.user.nickname, sessionScope.user.name)}</div>
                        <div class="">我的积分：<span class="score">0</span></div>
                    </div>
                </div>
                <ul class="profile-list">
                    <%--<li class="profile-des">--%>
                    <%--<div class="profile-btn description" style="display: none;">--%>
                    <%--<i class="fa fa-pencil"></i> <span>填写个人简介</span>--%>
                    <%--</div>--%>
                    <%--<div class="profile-des-edit" style="display: none;">--%>
                    <%--<textarea class="edit-textarea"></textarea>--%>
                    <%--<div class="profile-btn edit-btn">--%>
                    <%--<span>保存</span>--%>
                    <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="profile-des-txt">--%>
                    <%--<pre>为大家提供最新信息，总之就是编辑啦</pre>--%>
                    <%--</div>--%>
                    <%--</li>--%>
                    <li class="profile-btn upload">
                        <a href="${ctx}/doc/upload" onclick="return OBJ_bindAccount.checkBind(this)">
                            <i class="fa fa-upload"></i> <span >上传我的文档</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>

        <ul class="menu">
            <li class="menu-user">
                <a href="javascript:void(0);"><i class="icon-user"></i> 个人管理</a>
                <ul class="menu-sub">
                    <li><a href="${ctx}/user/center" onclick="return OBJ_bindAccount.checkBind(this)">个人中心</a></li>
                    <li><a href="${ctx}/user/profile" onclick="return OBJ_bindAccount.checkBind(this)">资料编辑</a></li>
                    <li><a href="${ctx}/user/pwd" onclick="return OBJ_bindAccount.checkBind(this)">修改密码</a></li>
                </ul>
            </li>
            <li class="menu-doc">
                <a href="javascript:void(0);"><i class="icon-book-open"></i> 文档管理</a>
                <ul class="menu-sub">
                    <li><a href="${ctx}/doc/myUpload" onclick="return OBJ_bindAccount.checkBind(this)">我的上传</a></li>
                    <li><a href="${ctx}/doc/myDownload" onclick="return OBJ_bindAccount.checkBind(this)">我的下载</a></li>
                    <li><a href="${ctx}/doc/myFav" onclick="return OBJ_bindAccount.checkBind(this)">我的收藏</a></li>
                </ul>
            </li>
            <li class="">
                <a href="${ctx}/auth/logout"><i class="icon-logout"></i> 退出登录</a>
            </li>
        </ul>
    </div>
</div>

