<jsp:include page="header.jsp"/>

<script>
    $(function () {
        setInterval(function () {
            $('.member .marquee dl:first').animate({
                marginTop: -$('.member .marquee dl:first').outerHeight()
            }, 500, 'swing', function () {
                $(this).removeAttr('style').insertAfter('.member .marquee dl:last');
            });
        }, 5000);
    })
</script>

<div class="content clearfix pd">
    <div class="container clearfix">
        <div class="index-main">
            <div class="category">
                <h4>推荐版块</h4>
                <div class="category-list clearfix">
                    <c:forEach items="${docCategories}" var="docCategory">
                        <dl>
                            <a href="${ctx}/doc/searchrecord?firstClassfy=${docCategory.id}" class="linkcolor linkover">
                                <dt><img src="${bt:url(docCategory.icon,"/files/" ,"assets/images/demo/logo1.png" )}"
                                         alt=""/></dt>
                                <dd>${docCategory.name}</dd>
                            </a>
                        </dl>
                    </c:forEach>
                </div>
            </div>

            <div class="recommend margintop40">
                <h4>每日推荐</h4>
                <div class="recommend-content clearfix">
                    <div class="recommend-list recommend-left">
                        <c:forEach items="${leftRecommendDocs}" var="doc">
                            <dl class="${doc.fileType} clearfix">
                                <dt><a class="linkline linkcolor"
                                       href="${ctx}/doc/detail/${doc.id}">${fn:escapeXml(doc.title)}</a>
                                </dt>
                                <dd>阅读：${doc.views}</dd>
                            </dl>
                        </c:forEach>
                    </div>
                    <div class="recommend-list recommend-right">
                        <c:forEach items="${rightRecommendDocs}" var="doc">
                            <dl class="${doc.fileType} clearfix">
                                <dt><a class="linkline linkcolor"
                                       href="${ctx}/doc/detail/${doc.id}">${fn:escapeXml(doc.title)}</a>
                                </dt>
                                <dd>阅读：${doc.views}</dd>
                            </dl>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <div class="doc-type margintop40">
                <h4>分类筛选</h4>
                <ul class="type-tab clearfix">
                    <li data-type="pdf" class="current">pdf文档</li>
                    <li data-type="doc">word文档</li>
                    <li data-type="excel">excel文档</li>
                    <li data-type="ppt">ppt文档</li>
                    <li data-type="txt">txt文档</li>
                    <li data-type="jpg">图片</li>
                </ul>
                <div>
                    <ul class="doc-type-list" id="pdf">
                        <c:forEach items="${pdfDocs}" var="doc">
                            <li>
                                <a href="${ctx}/doc/detail/${doc.id}"
                                   class="linkcolor linkover">
                                    <div class="cover vertical">
                                        <div class="cover-preview">
                                            <img src="${files}/${doc.imagePath}" alt=""/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(doc.title)}</div>
                                </a>
                                <div class="count">${doc.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                    <ul class="doc-type-list" id="doc" hidden="hidden">
                        <c:forEach items="${docDocs}" var="doc">
                            <li>
                                <a href="${ctx}/doc/detail/${doc.id}"
                                   class="linkcolor linkover">
                                    <div class="cover vertical">
                                        <div class="cover-preview">
                                            <img src="${files}/${doc.imagePath}" alt=""/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(doc.title)}</div>
                                </a>
                                <div class="count">${doc.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                    <ul class="doc-type-list" id="excel" hidden="hidden">
                        <c:forEach items="${xlsDocs}" var="doc">
                            <li>
                                <a href="${ctx}/doc/detail/${doc.id}"
                                   class="linkcolor linkover">
                                    <div class="cover">
                                        <div class="cover-preview">
                                            <img src="${files}/${doc.imagePath}" alt=""/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(doc.title)}</div>
                                </a>
                                <div class="count">${doc.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                    <ul class="doc-type-list" id="ppt" hidden="hidden">
                        <c:forEach items="${pptDocs}" var="doc">
                            <li>
                                <a href="${ctx}/doc/detail/${doc.id}"
                                   class="linkcolor linkover">
                                    <div class="cover horizontal">
                                        <div class="cover-preview">
                                            <img src="${files}/${doc.imagePath}" alt=""/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(doc.title)}</div>
                                </a>
                                <div class="count">${doc.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                    <ul class="doc-type-list" id="txt" hidden="hidden">
                        <c:forEach items="${txtDocs}" var="doc">
                            <li>
                                <a href="${ctx}/doc/detail/${doc.id}"
                                   class="linkcolor linkover">
                                    <div class="cover">
                                        <div class="cover-preview">
                                            <img src="${assets}/images/cover_txt.png" alt=""/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(doc.title)}</div>
                                </a>
                                <div class="count">${doc.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                    <ul class="doc-type-list" id="jpg" hidden="hidden">
                        <c:forEach items="${jpgDocs}" var="doc">
                            <li>
                                <a href="${ctx}/doc/detail/${doc.id}"
                                   class="linkcolor linkover">
                                    <div class="cover">
                                        <div class="cover-preview">
                                            <img src="${files}/${doc.filePath}" alt=""/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(doc.title)}</div>
                                </a>
                                <div class="count">${doc.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>

        <div class="index-side">
            <div class="member">
                <div class="member-header">
                    <h4>注册会员</h4>
                    <div class="count"><em>${userCount}</em>人</div>
                </div>
                <div class="member-content">
                    <div id="marquee" class="marquee">
                        <c:forEach items="${leftRecommendDocs}" var="doc">
                            <dl>
                                <dt>${fn:escapeXml(doc.diffTime)}前</dt>
                                <dd class="clearfix">
                                    <img src="${bt:url(doc.user.avatar,'files/','assets/images/demo/p1.jpg')}" alt=""/>
                                    <div class="member-info">
                                        <div class="member-name">
                                            <c:if test="${doc.uploaderType==1}">大牛数控</c:if>
                                            <c:if test="${doc.uploaderType!=1}">${bt:out(doc.user.nickname,doc.user.name)}</c:if>
                                        </div>
                                        <div class="member-act">上传文档《${fn:escapeXml(doc.title)}》</div>
                                    </div>
                                </dd>
                            </dl>
                        </c:forEach>
                        <c:forEach items="${rightRecommendDocs}" var="doc">
                            <dl>
                                <dt>${fn:escapeXml(doc.diffTime)}前</dt>
                                <dd class="clearfix">
                                    <img src="${bt:url(doc.user.avatar,'files/','assets/images/demo/p1.jpg')}" alt=""/>
                                    <div class="member-info">
                                        <div class="member-name">
                                            <c:if test="${doc.uploaderType==1}">大牛数控</c:if>
                                            <c:if test="${doc.uploaderType!=1}">${bt:out(doc.user.nickname,doc.user.name)}</c:if>
                                        </div>
                                        <div class="member-act">上传文档《${fn:escapeXml(doc.title)}》</div>
                                    </div>
                                </dd>
                            </dl>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <div class="hots margintop30">
                <h4>热门文档</h4>
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
    </div>
</div>

<%@include file="footer.jsp" %>
<script>
    $(function () {
        $(".doc-type").find("li").click(function () {
            $(this).addClass("current");
            $(this).siblings().removeClass("current");
            var type = $(this).attr("data-type");
            $("#" + type).show();
            $("#" + type).siblings().hide();
        })
    })
</script>