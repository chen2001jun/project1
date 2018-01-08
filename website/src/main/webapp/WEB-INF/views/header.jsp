<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <c:choose>
        <c:when test="${ !empty param.title }">
            <title>${param.title}</title>
            <c:if test="${ !empty param.keywords}">
                <meta name="keywords" content="${param.keywords }"/>
            </c:if>
            <c:if test="${ !empty param.description}">
                <meta name="description" content="${param.description }"/>
            </c:if>
        </c:when>
        <c:otherwise>
            <title>你身边的数控专家</title>
            <meta name="keywords"
                  content=""/>
            <meta name="description"
                  content=""/>
        </c:otherwise>
    </c:choose>
    <meta name="generator" content=""/>
    <meta name="author" content=""/>
    <link rel="stylesheet" href="${assets}/css/public.css" type="text/css">
    <link rel="stylesheet" href="${assets}/css/style.css" type="text/css">
    <link rel="stylesheet" href="${assets}/css/icheck-blue.css" type="text/css">
    <c:if test="${!empty param.css}">
        <c:forTokens items="${param.css}" delims="," var="c">
            <link rel="stylesheet" href="${assets}/${c}">
        </c:forTokens>
    </c:if>
    <script type="text/javascript" src="${assets}/js/min/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${assets}/js/min/icheck.min.js"></script>
    <script type="text/javascript" src="${assets}/js/website.js"></script>
    <script type="text/javascript" src="${assets}/js/layer.js"></script>
    <script type="text/javascript" src="${assets}/js/utils.js"></script>
    <script type="text/javascript" src="${assets}/js/manage.js"></script>
    <script type="text/javascript" src="${assets}/js/bindAccount.js"></script>
    <script type="text/javascript" src="${assets}/js/formcheck.js"></script>
    <script type="text/javascript" src="${assets}/js/loginBox.js"></script>
    <base href="${ctx}/">
    <script type="text/javascript">
        var ctx = '${ctx}';
        var isLogin = false;
        var isBind = false;
        <c:if test="${!empty sessionScope.user}">
        isLogin = true;
        if (${sessionScope.user.loginType == null}) {
            isBind = true;
        } else if (${sessionScope.user.isBind == true}) {
            isBind = true;
        }
        </c:if>
        console.log(${sessionScope.user.isBind});
        console.log(isBind);
    </script>
</head>

<body class="bg-white">
<div class="header">
    <div class="top">
        <div class="container">
            <div class="logo"></div>
            <div class="searchbar">
                <form action="${ctx}/doc/searchrecord">
                    <div class="searchbox clearfix">
                        <input class="search-input" type="text" name="content" id="searchWord"
                               placeholder="请输入您要搜索的关键词">
                        <button class="search-btn" type="submit" id="searchDoc"></button>
                    </div>
                </form>
                <div class="search-hots">
                    <span>热门搜索：</span>
                    <c:if test="${!empty pageContext.servletContext.getAttribute('hotSearchWords')}">
                        <c:forEach items="${pageContext.servletContext.getAttribute('hotSearchWords')}" var="word">
                            <a href="${ctx}/doc/searchrecord?content=${fn:escapeXml(word)}">${fn:escapeXml(word)}</a>
                        </c:forEach>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
    <div class="nav">
        <div class="container">
            <ul class="menu clearfix">
                <li><a href="${ctx}/">首页</a></li>
                <%--<li>--%>
                <%--<a class="icon-down" href="###"><span>分类</span></a>--%>
                <%--<ul class="dropdown-menu">--%>
                <%--<li><a href="###">下拉菜单一</a></li>--%>
                <%--<li><a href="###">下拉菜单二</a></li>--%>
                <%--<li><a href="###">下拉菜单三</a></li>--%>
                <%--<li><a href="###">下拉菜单四</a></li>--%>
                <%--</ul>--%>
                <%--</li>--%>
            </ul>
            <c:if test="${!empty sessionScope.user}">
                <div class="nav-toolbar clearfix">
                    <div class="userbar clearfix">
                        <a class="userdrop-toggle" href="javascript:void(0);">
                            <img class="portrait"
                                 src="${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}">
                            <span class="name">${bt:out(sessionScope.user.nickname, sessionScope.user.name)}</span>
                        </a>
                        <ul class="userbar-userdrop">
                            <div class="userbar-profile clearfix">
                                <img class="profile-avatar"
                                     src="${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}"
                                     alt=""/>
                                <div class="profile-info">
                                    <div class="profile-name">
                                            ${bt:out(sessionScope.user.nickname, sessionScope.user.name)}
                                    </div>
                                    <div class="profile-score">
                                        我的积分：<em class="score">0</em>
                                    </div>
                                </div>
                            </div>
                            <div class="userbar-box">
                                <div class="userbar-menu">
                                    <a class="menu-item orange" href="${ctx}/doc/upload"
                                       onclick="return OBJ_bindAccount.checkBind(this)">
                                        <i class="fa fa-upload"></i>
                                        <div>上传文档</div>
                                    </a>
                                    <a class="menu-item blue" href="${ctx}/user/center">
                                        <i class="fa fa-desktop"></i>
                                        <div>管理中心</div>
                                    </a>
                                    <a class="menu-item dark" href="${ctx}/auth/logout">
                                        <i class="fa fa-sign-out"></i>
                                        <div>退出</div>
                                    </a>
                                </div>
                            </div>
                        </ul>
                    </div>
                </div>
            </c:if>
            <c:if test="${empty sessionScope.user}">
                <div class="nav-toolbar clearfix">
                    <ul class="iconbar clearfix">
                        <a href="${ctx}/auth/login" title="登录">
                            <li><i class="icon-login"></i> <em>登录</em></li>
                        </a>
                        <a href="${ctx}/auth/register" title="注册">
                            <li><i class="icon-user-follow"></i> <em>注册</em></li>
                        </a>
                    </ul>
                </div>
            </c:if>
        </div>
    </div>
</div>
<!--弹框绑定手机号-->
<div id="accountDialog" style="padding: 20px; width: 420px; display: none;">
    <div class="dialog-header">您需要绑定站内账号，才能够执行该操作</div>
    <form class="dialog-form" >
        <div class="row">
            <label>手机号：</label>
            <div class="item">
                <div class="clearfix">
                    <c:if test="${sessionScope!=null&&sessionScope.user!=null}">
                        <input id="type" value="${sessionScope.user.loginType}" hidden/>
                        <input id="openid" value="${sessionScope.user.openid}" hidden/>
                    </c:if>
                    <input class="ui-input-default phonenumber" id="mobileBind" name="mobile" type="text"
                           onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="11"
                           onchange="OBJ_formcheck.checkFormatBind('bindMgs','phone','手机号码格式不对');"/><a class="button-tiny"
                                                                                                    id="bind_sendmsg_button"
                                                                                                    type="button"
                                                                                                    onclick="OBJ_bindAccount.sendMsg('mobileBind');">获取验证码</a>
                    <a class="button-tiny" id="bind_msg_end" type="button"
                       style="display:none;"><span id="bind_sendmsg_time_text">60</span>秒后重发</a>
                </div>
            </div>
        </div>

        <!--
        <div class="row form-group success">
            <label for="phone">手机号：</label>
            <div class="item">
            	<div class="clearfix">
                	<input class="ui-input-default phonenumber" id="phone" type="text" /><a class="button-tiny">获取验证码</a>
                </div>
                <div class="txt"><i class="fa fa-check"></i> 正确提示信息</div>
            </div>
        </div>
        -->

        <div class="row">
            <label >验证码：</label>
            <div class="item">
                <div><input class="ui-input-default" id="code" type="text" onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')"
                            maxlength="6"
                            onclick="OBJ_bindAccount.sendMsg('username');"/></div>
            </div>
        </div>

        <div class="row">
            <label for="password">密码：</label>
            <div class="item">
                <div><input class="ui-input-default" id="password" type="password" onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')" placeholder="请输入6-20位有效密码"
                            maxlength="20"/></div>
            </div>
        </div>
        <div id="bindMgs" class="row ">
            <label></label>
            <div class="item">
                <span id="bindMgs_tips" class="txt"></span>
            </div>
        </div>
        <div class="row">
            <label></label>
            <div class="item">
                <button class="button btn-large btn-primary" type="button" onclick="OBJ_bindAccount.bind()">绑定账号
                </button>
            </div>
        </div>
    </form>
</div>
<!--弹框登录-->
<div id="loginDialog" style="padding: 20px; width: 420px; display: none;">
    <form class="dialog-form ">
        <div class="row">
            <label >手机号：</label>
            <div class="item">
                <div><input class="ui-input-default" id="mobileLogin" name="mobile" type="text" placeholder="请输入11位手机号码"
                            onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="11"
                            onchange="OBJ_formcheck.checkFormatLogin('loginMgs','phone','手机号码格式不对');"/></div>
            </div>
        </div>

        <div class="row">
            <label for="password">密码：</label>
            <div class="item">
                <div><input class="ui-input-default" id="passwordLogin" type="password"
                            onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')" placeholder="请输入6-20位有效密码"
                            maxlength="20"/></div>
            </div>
        </div>


        <div class="row">
            <label for="password">验证码：</label>
            <div class="item">
                <div><input class="ui-input-default" id="captchaLogin" placeholder="看不清？请点击图片"
                            type="text" style="width: 160px;" onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')"
                            maxlength="6"/><img id="getCaptcha" class="captcha" src="${ctx}/assets/captcha.jpg"
                                                alt="点击图片更新" style="width: 100px;"/></div>
            </div>
        </div>
        <div id="loginMgs" class="row ">
            <label></label>
            <div class="item">
                <span id="loginMgs_tips" class="txt"></span>
            </div>
        </div>
        <div class="row">
            <label></label>
            <div class="item clearfix">
                <span class="fl"><input class="" type="checkbox" name="remember"/> 记住密码</span>
                <span class="fr"><a href="${ctx}/user/forgotPwd" target="_blank">忘记密码？</a></span>
            </div>
        </div>

        <div class="row">
            <label></label>
            <div class="item">
                <button class="button btn-primary" type="button" onclick="OBJ_loginBox.login()">登录</button>
                &nbsp;
                <button class="button btn-orange" type="button" onclick="window.open('${ctx}/auth/register')"> 注册
                </button>
            </div>
        </div>
    </form>
</div>
