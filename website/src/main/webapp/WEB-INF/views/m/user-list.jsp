<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title>个人信息</title>
</head>
<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="${ctx}/m/user/center">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu"></div>
        <div class="title">个人</div>
    </header>
    <div class="content clearfix">
        <div class="setting-list">
            <a href="###">版权声明</a>
            <a href="${ctx}/m/user/repwd">修改密码</a>
            <a class="btn-logout" href="${ctx}/m/auth/logout" methods="get">退出登录</a>
        </div>
    </div>
    <footer></footer>
</div>
</body>
</html>
