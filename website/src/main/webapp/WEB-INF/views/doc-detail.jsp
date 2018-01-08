<jsp:include page="header.jsp">
    <jsp:param name="title" value="${fn:escapeXml(doc.title)}"/>
    <jsp:param name="keywords" value="${fn:escapeXml(doc.title)}"/>
    <jsp:param name="description" value="${fn:escapeXml(doc.title)} ${fn:escapeXml(doc.summary)}"/>
    <jsp:param name="css" value="css/jquery.Jcrop.css"/>
</jsp:include>

<div class="content bg-white pd-b">
    <div class="container">
        <div class="page-doc clearfix">
            <div class="doc-header">
                <div class="doc-title ${doc.fileType}">${fn:escapeXml(doc.title)}<small>.${doc.fileType}</small></div>
                <div class="doc-meta clearfix">
                    <div class="meta-pic"><img src="${bt:url(doc.user.avatar,'files/','assets/images/demo/p1.jpg')}" alt=""/></div>
                    <div class="meta-info">
                        <div class="clearfix">
                            <div class="meta-info-name">
                                <c:if test="${!empty doc.user}">${bt:out(doc.user.nickname, doc.user.name)}</c:if>
                                <c:if test="${empty doc.user}">管理员</c:if>
                            </div>
                            <ul class="meta-info-nums clearfix">
                                <li><em>阅读：</em>${doc.views}</li>
                                <li><em>积分：</em>${doc.costScore}</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="doc-content clearfix">
                <div class="main">
                    <div class="doc-reader">
                        <div class="reader-content">
                            <div class="reader-toolbar clearfix">
                                <a id="download" class="reader-btn btn-download" href="javascript:;" onclick="OBJ_loginBox.checkLogin(operate.clickDownload)"><i class="fa fa-cloud-download"></i> 下载</a>
                                <c:if test="${isCollect=='unLogin'}">
                                    <a id="collect" class="reader-btn btn-fav" href="javascript:;" onclick="OBJ_loginBox.checkLogin(operate.clickCollect)"><i class="fa fa-star"></i> 收藏</a></c:if>
                                <c:if test="${isCollect=='notCollect'}">
                                    <a id="collect" class="reader-btn btn-fav" href="javascript:;" onclick="OBJ_loginBox.checkLogin(operate.clickCollect)"><i class="fa fa-star"></i> 收藏</a></c:if>
                                <c:if test="${isCollect=='collect'}">
                                    <a id="collect" class="reader-btn btn-fav" href="javascript:;" onclick="OBJ_loginBox.checkLogin(operate.clickCollect)"><i class="fa fa-star-o"></i> 取消收藏</a></c:if>
                                <div class="reader-count">页数：<span class="current">1</span>/<c:if test="${images.size()!=0}">${images.size()}</c:if><c:if test="${images.size()==0}">1</c:if></div>
                            </div>
                            <div class="reader-box">
                                <div class="reader-box-wrapper">
                                    <c:if test="${images.size()!=0}">
                                        <c:forEach items="${images}" var="image">
                                            <img class="reader-page" src="${assets}/images/grey.gif" data-original="${files}/${image.path}" alt=""/>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${doc.fileType.matches('jpg|jpeg|png|gif|bmp|ico')}">
                                        <div class="pics">
                                            <img class="reader-page" src="${files}/${doc.filePath}" alt=""/>
                                        </div>
                                    </c:if>
                                    <c:if test="${!empty content}">
                                        <div style="padding: 30px 10px">${content}</div>
                                    </c:if>
                                    <div class="reader-finish">
                                        <ul class="finish-list">
                                            <li>阅读已结束，如果下载本文需要使用 <span class="color-red">${doc.costScore}积分</span></li>
                                            <li>
                                                <a href="javascript:;" onclick="OBJ_loginBox.checkLogin(operate.clickDownload)" class="button btn-blue btn-large btn-download margintop15">下载文档</a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="aside">
                    <div class="aside-wrapper">
                        <div class="widget">
                            <div class="widget-header">相关文档推荐</div>
                            <div class="widget-content">
                                <c:forEach items="${recommendList}" var="d">
                                    <div class="widget-item ${d.fileType}"><a class="linkcolor linkline" href="${ctx}/doc/detail/${d.id}">${d.title}</a></div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="widget margintop30">
                            <div class="widget-header">热门文档</div>
                            <div class="widget-content">
                                <div class="hots-list">
                                    <c:forEach items="${hotDocs}" var="doc" varStatus="docStatus">
                                        <dl class="clearfix">
                                            <dt>${docStatus.count}</dt>
                                            <dd>
                                                <a href="${ctx}/doc/detail/${doc.id}"
                                                   class="linkcolor linkover">
                                                    <div class="hots-title">${fn:escapeXml(doc.title)}</div>
                                                </a>
                                                <div class="count">${doc.views}人阅读</div>
                                            </dd>
                                        </dl>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <a href="#0" class="cd-top">Top</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="${assets}/js/min/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${assets}/js/lrtk.js"></script>
<%@include file="footer.jsp" %>

<script type="text/javascript">

    //各项操作
    var id = ${doc.id};
    var score = ${doc.costScore};
    function doDownload(){
        $.ajax({
            type: 'get',
            url: '${ctx}/doc/dl/' + id,
            success: function (data) {
                window.location.href = "${ctx}/doc/dl?c=" + data['data'];
            },
            error: function (req) {
                var errormsg = JSON.parse(req.responseText);
                if (errormsg) {
                    layer.alert(errormsg.message, {icon: 2, time: 2000});
                } else {
                    layer.alert('下载失败');
                }
                console.warn(req);
//                alert(req['responseJSON'].message || '下载失败');
            }
        });
    }
    var operate = {
        clickDownload: function(){
            if (score > 0) {
                var down = layer.confirm('当前文档需要消耗【' + score + '】积分,如果已经下载过，则不再扣积分，确认下载吗？',
                        {btn: ['确定', '取消'], icon: 3}, function () {
                            layer.close(down);
                            doDownload();
                        })
            }
            if (score == 0) {
                doDownload();
            }
        },
        clickCollect: function(){
            var type = $('#collect').html();
            if (type == '<i class="fa fa-star"></i> 收藏') {
                $.ajax({
                    type: 'get',
                    url: '${ctx}/doc/collect?id=' + id,
                    success: function (data) {
                        $('#collect').html('<i class="fa fa-star-o"></i> 取消收藏');
                    },
                    error: function (req) {
                        console.log(req);
                        layer.alert("操作失败！");
                    }
                });
            } else {
                $.ajax({
                    type: 'get',
                    url: '${ctx}/doc/uncollect?id=' + id,
                    success: function (data) {
                        $('#collect').html('<i class="fa fa-star"></i> 收藏');
                    },
                    error: function (req) {
                        console.log(req);
                        layer.alert("操作失败！");
                    }
                });
            }
        }
    };
    $(document).scroll(function(){
        if($(document).scrollTop() > $('.reader-content').offset().top){
            $('.reader-content .reader-toolbar').addClass('fixed').css('top',0);
        }else{
            $('.reader-content .reader-toolbar').removeClass('fixed').css('top',0);
        }

        if($(document).scrollTop() > $('.aside').offset().top){
            $('.aside .aside-wrapper').addClass('fixed').css('top',0);
        }else{
            $('.aside .aside-wrapper').removeClass('fixed').css('top',0);
        }

        if($(document).scrollTop() > ($('.doc-content').offset().top + $('.doc-content').height() - $('.aside .aside-wrapper').height())){
            $('.aside .aside-wrapper').removeClass('fixed').css('top', $('.doc-content').height() - $('.aside .aside-wrapper').height());
        }
    });

    $(function(){
        //导航容器宽
        var wrapperWidth = $('.reader-content').width() - parseInt($('.reader-content .reader-toolbar').css('borderLeftWidth')) - parseInt($('.reader-content .reader-toolbar').css('borderRightWidth'));
        $('.reader-content .reader-toolbar').css('width',wrapperWidth);

        //计算当前浏览图片页码
        //动画速度
        var speed = 250,
                offsetFix = 0;
        $(document).scroll(function(){
            $('.reader-box .reader-page').each(function(index, element) {
                var $ctx = $(this);
                rangeTop = parseInt($(element).offset().top - offsetFix,10),
                        rangeBottom = parseInt($(element).offset().top + $(element).height(),10);
                if($(document).scrollTop() >= rangeTop && $(document).scrollTop() <= rangeBottom){
                    $('.reader-count .current').text(index + 1);
                }
            });
        });

        //图片懒加载
        $('.reader-box img').lazyload();
    })
</script>