<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/swiper.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/min/jquery.marquee.min.js"></script>
    <script type="text/javascript" src="${assets}/js/min/swiper.jquery.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title><spring:message code="site.name"/></title>
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
            <a class="current" href="${ctx}/">首页</a>
            <a href="${ctx}/m/doc/category">分类</a>
            <a href="${ctx}/m/user/center">我的</a>
        </div>
    </header>

    <div class="content bg-white clearfix">
        <div class="page">
            <%--<c:if test="${downloads.size()>0}">--%>
            <ul id="marquee" class="marquee">
                <c:forEach items="${downloads}" var="download">
                    <li><span class="username">${fn:escapeXml(download.uNickname)}</span> 上传了文档 《${fn:escapeXml(download.dTitle)}》</li>
                </c:forEach>
            </ul>
            <%--</c:if>--%>

            <div class="searchbar">
                <form action="${ctx}/m/doc" method="get">
                    <div class="searchbox">
                        <button type="submit" class="searchbtn" id="searchbtn" href="javascript:;"><i
                                class="fa fa-search"></i></button>
                        <div class="searchinput-wrapper">
                            <input class="searchinput" name="content" type="text" placeholder="输入搜索内容"
                                   value="${fn:escapeXml(param.content)}">
                        </div>
                    </div>
                </form>
                <div class="search-hots">
                    <span>热门搜索：</span>
                    <c:if test="${!empty pageContext.servletContext.getAttribute('hotSearchWords')}">
                        <c:forEach items="${pageContext.servletContext.getAttribute('hotSearchWords')}" var="word">
                            <a href="${ctx}/m/doc?content=${fn:escapeXml(word)}">${fn:escapeXml(word)}</a>
                        </c:forEach>
                    </c:if>
                </div>
            </div>

            <div class="section">
                <h4>推荐板块 <span class="color-grey">(左右滑动)</span></h4>
                <div class="section-content">
                    <div class="recommend-category swiper-container">
                        <ul class="category-wrapper swiper-wrapper clearfix">
                            <c:forEach items="${categorys}" var="category">
                                <li class="swiper-slide">
                                    <a href="${ctx}/m/doc/category/${category.id}">
                                        <div class="category-ico">
                                            <c:if test="${category.icon==null}">
                                                <img src="${assets}/images/logo.jpg" alt=""/>
                                            </c:if>
                                            <c:if test="${category.icon!=null}">
                                                <img src="${files}/${category.icon}" alt=""/>
                                            </c:if>
                                        </div>
                                        <div class="category-txt">${fn:escapeXml(category.name)}</div>
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="section">
                <h4>为您推荐</h4>
                <div class="section-content">
                    <div class="doc-list">
                        <c:forEach items="${docs.content}" var="doc">
                            <a class="${doc.fileType} clearfix"
                               href="${ctx}/m/doc/${doc.id}">
                                <div class="doc-icon"></div>
                                <div class="doc-content">
                                    <div class="doc-title">${fn:escapeXml(doc.title)}</div>
                                    <div class="doc-meta">
                                        <span>大小：<bt:fileSize size="${doc.fileSize}"/></span>
                                        <span>阅读：${doc.views}</span>
                                            <%--<span>上传：${doc.user.name}</span>--%>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div>
        <span id="qqLoginBtn" style="margin:20px 0;"></span>
    </div>

    <footer>
    </footer>
</div>
<script>
    $(document).ready(function () {
        $("#marquee").marquee({yScroll: "bottom", showSpeed: 1000});

        var swiper = new Swiper('.swiper-container', {
            pagination: '.swiper-pagination',
            slidesPerView: 4,
            paginationClickable: true
        });
    });
</script>
</body>
</html>
