<%--suppress ALL --%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title>用户中心</title>
    <base href="${ctx}/">
</head>

<body>
<div class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a class="menu-item"><i class="fa fa-bars"></i></a>
            </div>
            <div class="menu-right">
                <a class="menu-item" href="${ctx}/m/doc/reportWord"><i class="fa fa-reportWord"></i></a>
            </div>
            <div class="title">CNC数控技术</div>
        </div>
        <div class="tabs clearfix">
            <a href="${ctx}/m/">首页</a>
            <a href="${ctx}/m/doc/category">分类</a>
            <a class="current">我的</a>
        </div>
    </header>
    <div class="content bg-white clearfix">
        <div class="page">
            <c:if test="${empty sessionScope.user}">
                <div class="profile clearfix">
                    <div class="profile-photo default">
                        <img src="${assets}/images/default_avatar.png"/>
                    </div>
                    <div class="profile-info">
                        <div class="profile-title">游客</div>
                        <a class="btn-login" href="${ctx}/m/auth/login?backurl=${ctx}/m/user/center">登录</a>
                    </div>
                </div>
            </c:if>
            <c:if test="${!empty sessionScope.user}">
                <div class="profile clearfix">
                    <div class="profile-photo">
                        <img src="${bt:url(user.avatar, 'files/', 'assets/images/default_avatar.png')}"/>
                    </div>
                    <ul class="profile-info">
                        <li class="profile-title">
                            <c:out value="${user.nickname}">${user.name}</c:out>
                            <a class="fr"><i class="fa fa-cog"></i></a></li>
                        <li class="profile-account">${user.mobile}</li>
                        <li>积分：<span class="color-red">${userScore.totalScore}</span></li>
                    </ul>
                </div>
                <div class="section">
                    <ul class="tabs clearfix">
                        <li class="current"><i class="fa fa-star"></i> 我的收藏</li>
                        <li><i class="fa fa-cloud-download"></i> 我的下载</li>
                    </ul>
                    <div class="section-content">
                        <ul class="doc-favlist">
                            <c:forEach items="${collects}" var="c">
                                <li class="${c.fileType} clearfix" data-docid= ${c.id}>
                                    <a class="doc-btn">
                                        <div class="btn-unfav"><i class="fa fa-star-o"></i></div>
                                        <div class="btn-txt">取消</div>
                                    </a>
                                    <a class="doc-content" href="${ctx}/m/doc/${c.id}">
                                        <div class="doc-icon"></div>
                                        <div class="doc-info">
                                            <div class="doc-title">${c.title}</div>
                                            <div class="doc-meta">
                                                <span>大小：<bt:fileSize size="${c.fileSize}"/></span>
                                                <span>阅读：${c.views}</span>
                                            </div>
                                        </div>
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
    <footer></footer>
</div>
<script type="text/javascript">
    var ctx = '${ctx}';
    function fileSize(size) {
        if (size < 1024) {
            return size + "KB";
        } else if (size < 1024 * 1024) {
            return (size / 1024).toFixed(2) + 'MB';
        } else {
            return (size / (1024 * 1024)).toFixed(2) + 'GB';
        }
    }
    $('body').on('click', 'ul.tabs li:not(.current)', function () {
        var $this = $(this);
        var text = $this.text().trim();
        if (text == '我的收藏') {
            $.get(ctx + '/m/user/myCollects', function (data) {
                $('.section-content ul').removeClass('doc-list').addClass('doc-favlist').empty();
                data.forEach(function (d) {
                    $('.doc-favlist').append(
                            '<li class="' + d['fileType'] + ' clearfix" data-docid= ' + d['id'] + '>' +
                            '<a class="doc-btn">' +
                            '<div class="btn-unfav"><i class="fa fa-star-o"></i></div>' +
                            '<div class="btn-txt">取消</div>' +
                            '</a>' +
                            '<a class="doc-content" href="' + ctx + '/m/doc/' + d['id'] + '">' +
                            '<div class="doc-icon"></div>' +
                            '<div class="doc-info">' +
                            '<div class="doc-title">' + d['title'] + '</div>' +
                            '<div class="doc-meta">' +
                            '<span>大小：' + fileSize(d['fileSize']) + '</span>' +
                            '<span>阅读：' + d['views'] + '</span>' +
                            '</div>' +
                            '</div>' +
                            '</a>' +
                            '</li>');
                });
                $('ul.tabs li.current').removeClass('current');
                $this.addClass('current');
            });
        } else {
            $.get(ctx + '/m/user/myDownloads', function (data) {
                $('.section-content ul').removeClass('doc-favlist').addClass('doc-list').empty();
                data.forEach(function (d) {
                    $('.section-content ul').append(
                            '<a class="' + d['fileType'] + ' clearfix" href="' + ctx + '/m/doc/' + d['id'] + '">' +
                            '<div class="doc-icon"></div>' +
                            '<div class="doc-content">' +
                            '<div class="doc-title">' + d['title'] + '</div>' +
                            '<div class="doc-meta">' +
                            '<span>大小：' + fileSize(d['fileSize']) + '</span>' +
                            '<span>阅读：' + d['views'] + '</span>' +
                            '</div>' +
                            '</div>' +
                            '</a>');
                });
                $('ul.tabs li.current').removeClass('current');
                $this.addClass('current');
            });
        }
    });
    $('body').on('click', '.doc-btn', function () {
        var $this = $(this);
        var docid = $this.parent().attr('data-docid');
        $.ajax({
            url: ctx + '/doc/uncollect?id=' + docid,
            type: 'get',
            success: function () {
                $this.parent().remove();
            },
            error: function (r) {
                console.log(r);
            }
        });
    });

    $('.fr').click(function () {
        location.href = ctx + '/m/user/list';
    });
</script>
</body>
</html>
