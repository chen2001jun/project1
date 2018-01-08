<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0"/>
    <title>${fn:escapeXml(doc.title)}</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
</head>

<body>
<div class="container">
    <div class="zoomable">
        <header>
            <a id="prevPage" class="prev" href="javascript:history.back()">
                <i class="fa fa-chevron-left"></i>
            </a>

            <div class="menu">
            </div>

            <div class="title">
                <c:if test="${category==null}"> CNC数控技术</c:if>
                <c:if test="${category!=null}"> ${category.name}</c:if>
            </div>
        </header>

        <div class="content clearfix">
            <article>
                <div class="doc-header">
                    <h1>${fn:escapeXml(doc.title)}</h1>

                    <div class="btns-bar clearfix">
                        <span class="filetype">${doc.fileType}文件</span>
                        <c:if test="${empty sessionScope.user}">
                            <a id="download" href="${ctx}/m/auth/login?backurl=${ctx}/m/doc/${doc.id}"><i class="fa fa-cloud-download"></i> 登录后下载</a>
                        </c:if>
                        <c:if test="${isExists && !empty sessionScope.user}">
                            <a id="download" href="javascript:;"><i class="fa fa-cloud-download"></i> 下载</a>
                        </c:if>
                        <c:if test="${isCollect=='notCollect'}"> <a id="collect" href="javascript:;"><i
                                class="fa fa-star"></i> 收藏</a></c:if>
                        <c:if test="${isCollect=='collect'}"><a id="collect" href="javascript:;"><i
                                class="fa fa-star-o"></i> 取消收藏</a></c:if>
                    </div>
                </div>

                <div class="meta">

                    <ul class="doc-meta clearfix">
                        <li><strong>阅读：</strong>${doc.views}</li>
                        <li><strong>上传者：</strong>${doc.user.nickname}</li>
                        <li><strong>积分：</strong><span class="color-red">${doc.costScore}</span></li>
                    </ul>
                    <ul class="meta-list">
                        <!--<p><strong>关键字：</strong>90,DS1449,</p>-->
                        <li>${doc.summary}</li>
                    </ul>
                </div>
                <c:if test="${images!=null}">
                    <div class="pics">
                        <c:forEach items="${images}" var="image">
                            <img class="image-page" src="${files}/${image.path}" alt=""/>
                        </c:forEach>
                    </div>
                </c:if>
                <c:if test="${!empty content}">
                    <div class="pics" style="text-indent: 2em;">
                        ${content}
                    </div>
                </c:if>
                <c:if test="${doc.fileType.matches('jpg|png|gif|bmp|ico')}">
                    <div class="pics">
                        <img class="image-page" src="${files}/${doc.filePath}" alt=""/>
                    </div>
                </c:if>
                <!-- 分页 -->
                <%--<bt:pagination data="${docs}"/>--%>

                <div class="qr">
                    <img src="${assets}/images/qr.jpg" alt=""/>

                    <p>扫描二维码关注我们</p>
                </div>
            </article>
        </div>
        <footer>
        </footer>
    </div>
</div>
</body>
<script>
    var id = ${doc.id};
    var isLogin = ${sessionScope.user == null ? 'false' : 'true'};
    var score = ${doc.costScore};
    $('#download').click(function () {
        if (!isLogin) {
            window.location.href = '${ctx}/m/user/center?docId=' + id;
        } else {
            if (score > 0 && !confirm('当前文档需要消耗【' + score + '】积分,如果已经下载过，则不再扣积分，确认下载吗？')) {
                return;
            }
            $.ajax({
                type: 'get',
                url: '${ctx}/doc/dl/' + id,
                success: function (data) {
                    window.location.href = "${ctx}/doc/dl?c=" + data['data'];
                },
                error: function (req) {
                    console.warn(req);
                    alert(req['responseJSON'].message || '下载失败');
                }
            });
        }
    });

    $('#collect').click(function () {
        var type = $('#collect').html();
        var id =${doc.id};
        if (type == '<i class="fa fa-star"></i> 收藏') {
            $.ajax({
                type: 'get',
                url: '${ctx}/doc/collect?id=' + id,
                success: function (data) {
                    $('#collect').html('<i class="fa fa-star-o"></i> 取消收藏');
                },
                error: function (req) {
                    console.log(req);
                    alert("操作失败！");
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
                    alert("操作失败！");
                }
            });
        }
    });
</script>
</html>
