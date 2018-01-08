<jsp:include page="../header.jsp"/>

<div class="content pd20 management">
    <div class="container">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="doc">
            <ul class="subnav clearfix">
                <li class="current"><a href="${ctx}/doc/myUpload">我的上传</a></li>
                <li><a href="${ctx}/doc/myDownload">我的下载</a></li>
                <li><a href="${ctx}/doc/myFav">我的收藏</a></li>
            </ul>
            <div class="box clearfix">
                <div class="box-content">
                    <form action="${ctx}/doc/docDelete" id="docsDeleteId" method="post">
                        <table class="table-style2 dyntable">
                            <thead>
                            <tr>
                                <th><input class="checkall" type="checkbox"/></th>
                                <th>名称</th>
                                <th>文档状态</th>
                                <th>类型</th>
                                <th>创建日期</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${docs}" var="doc">
                                <c:if test="${doc.state!=9}">
                                    <tr>
                                        <td><input name="docIds" type="checkbox" value="${doc.id}"/></td>
                                        <td>${fn:escapeXml(doc.title)}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${doc.state == 1}"><span class="label label-green">已公开</span></c:when>
                                                <c:when test="${doc.state == 2}"><span class="label label-primary">审核中</span></c:when>
                                                <c:otherwise><span class="label lable-red">审核不通过</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${doc.fileType}</td>
                                        <td><fmt:formatDate value="${doc.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td>
                                            <c:if test="${doc.state == 1}">
                                            <a href="${ctx}/doc/detail/${doc.id}">查看</a>
                                            </c:if>
                                            <a class="marginleft10" href="javascript:void(0);" onclick="docDelete(${doc.id})">删除</a>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            <%--<tr>--%>
                            <%--<td><input class="" name="" type="checkbox" value=""/></td>--%>
                            <%--<td>芹菜煮水的特殊功效</td>--%>
                            <%--<td><span class="label label-green">已公开</span></td>--%>
                            <%--<td>上传</td>--%>
                            <%--<td>2016-08-24</td>--%>
                            <%--<td><a href="#">查看</a><a class="marginleft10" href="#">删除</a></td>--%>
                            <%--</tr>--%>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" rowspan="1"><a href="javascript:void(0);"
                                                               onclick="docsDelete()">批量删除</a></td>
                            </tr>
                            </tfoot>
                        </table>
                    </form>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/jquery.dataTables.js"></script>
<%@include file="../footer.jsp" %>
<script>
    $(function () {
        //上传选择栏
        $('.fileGroup .fileSelect').click(function (e) {
            var $group = $(this).parent('.fileGroup');
            $group.find('.fileInput').click().change(function () {
                $group.find('.filePath').val($(this).val());
            })
        });
    })
    function docDelete(id) {
        layer.confirm("确定删除记录？", {btn: ['确定', '取消'], icon: 3}, function () {
            $.ajax({
                url: ctx + "/doc/docDelete",
                data: {docIds: id},
                type: 'post',
                success: function () {
                    location.href = ctx + "/doc/myUpload";
                },
                error: function () {
                    layer.alert("删除失败");
                }
            });
        });
    }
    function docsDelete() {
        var myUps = $("div .checked").children("input");
        if (myUps.length == 0) {
            return;
        }
        layer.confirm("确定删除记录？", {btn: ['确定', '取消'], icon: 3}, function () {
            $("#docsDeleteId").submit();
        })
    }
</script>