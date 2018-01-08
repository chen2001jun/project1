<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>CNC数控文库搜索</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
</head>

<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="${ctx}/m/">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu">
        </div>

        <div class="title">CNC数控技术</div>
    </header>

    <div class="content clearfix">
        <div class="doc-search">
            <div class="searchbar">
                <form action="${ctx}/m/doc" method="get">
                    <div class="searchbox">
                        <button type="submit" class="searchbtn" id="searchbtn">
                            <i class="fa fa-search"></i>
                        </button>
                        <div class="searchinput-wrapper">
                            <input class="searchinput" name="content" type="text" placeholder="输入搜索内容"
                                   value="${fn:escapeXml(param.content)}">
                        </div>
                    </div>
                </form>
                <div class="search-hots">
                    <span>热门搜索：</span>
                    <c:forEach items="${searchWords.content}" var="searchWord">
                        <a href="${ctx}/m/doc?content=${fn:escapeXml(searchWord.name)}">${fn:escapeXml(searchWord.name)}</a>
                    </c:forEach>
                </div>
            </div>
            <c:if test="${docs.content.size()>0}">
                <dl class="record-count">
                    <c:if test="${param.content != null && param.content != ''}">
                        <dt>与“<em>${param.content}</em>”相关的文档</dt>
                    </c:if>
                    <dd>共${docs.totalPages}页， ${docs.totalElements} 篇文章</dd>
                </dl>
                <div class="record-list">
                    <c:forEach items="${docs.content}" var="doc" varStatus="i">
                        <dl class="clearfix">
                            <dt>${i.count}</dt>
                            <dd><a href="${ctx}/m/doc/${doc.id}">${doc.title}</a></dd>
                        </dl>
                    </c:forEach>
                </div>
                <bt:pagination data="${docs}"/>
            </c:if>
        </div>
    </div>

    <footer>
    </footer>
</div>
</body>
</html>
