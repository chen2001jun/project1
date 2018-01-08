<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <meta name="title" content="${article.title}">
    <meta name="summary" content="${article.summary}">
    <meta name="keywords" content="${article.tags}">
    <title>${article.title}</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/pinchzoom.js"></script>
</head>

<body>
<div class="container">
    <div class="zoomable">
        <header>
            <div class="logo">
                <img src="${assets}/images/logo.jpg"/>
            </div>
            <div class="header-info">
                <h1>数控资料查询</h1>
                <p>随身的技术手册--数控技术人员的好帮手</p>
            </div>
        </header>

        <div class="content clearfix">
            <article>
                <h1>${article.title}</h1>
                <div class="meta">
                    <p>
                        <a class="download" href="${ctx}/files/${article.pdf}">[下载文档]</a>
                    </p>
                    <p><strong>关键字：</strong>${article.tags}</p>
                    <p><strong>简介：</strong><br/>${article.summary}</p>
                    <p><strong>详情：</strong></p>
                </div>
                <c:forEach items="${images}" var="image">
                    <div class="pics">
                        <img src="${ctx}/files/${image}"/>
                    </div>
                </c:forEach>
                <div class="qr">
                    <img src="${assets}/images/qr.jpg"/>
                    <p>扫描二维码关注我们</p>
                </div>
            </article>
        </div>

        <footer>
        </footer>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
    var title = '${article.title}';
    var link = location.href;
    var imgUrl = 'http://www.d6sk.com/assets/images/logo.jpg';
    var desc = '${article.summary}';

    $.post('${ctx}/weixin/signature', {url: location.href}, function (data) {
        wx.config({
            debug: false,
            appId: data.appId,
            timestamp: data.timestamp,
            nonceStr: data.noncestr,
            signature: data.signature,
            jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone']
        });
    });
    wx.ready(function () {
        wx.onMenuShareTimeline({    // 朋友圈
            title: title,
            link: link,
            imgUrl: imgUrl,
            success: function () {
                console.log('朋友圈分享成功！');
            },
            cancel: function () {
                console.log('朋友圈分享取消！');
            }
        });
        wx.onMenuShareAppMessage({ // 朋友
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('朋友分享成功！');
            },
            cancel: function () {
                console.log('朋友分享取消！');
            }
        });
        wx.onMenuShareQQ({ // QQ
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('QQ分享成功！');
            },
            cancel: function () {
                console.log('QQ分享取消！');
            }
        });
        wx.onMenuShareWeibo({ // 微博
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('微博分享成功！');
            },
            cancel: function () {
                console.log('微博分享取消！');
            }
        });
        wx.onMenuShareQZone({ // QZone
            title: title,
            link: link,
            imgUrl: imgUrl,
            desc: desc,
            success: function () {
                console.log('QZone分享成功！');
            },
            cancel: function () {
                console.log('QZone分享取消！');
            }
        });
    });
    wx.error(function (res) {
        console.log(res);
    });
</script>
</body>

<script>
    $(function () {
        $('.zoomable').each(function () {
            new RTP.PinchZoom($(this), {});
        });
    })
</script>
</html>




