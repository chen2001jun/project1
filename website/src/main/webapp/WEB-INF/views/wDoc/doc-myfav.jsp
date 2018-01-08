<jsp:include page="../header.jsp"/>

<div class="content pd20 management">
    <div class="container">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="doc">
            <ul class="subnav clearfix">
                <li><a href="${ctx}/doc/myUpload">我的上传</a></li>
                <li><a href="${ctx}/doc/myDownload">我的下载</a></li>
                <li class="current"><a href="${ctx}/doc/myFav">我的收藏</a></li>
            </ul>
            <div class="box clearfix">
                <div class="box-content">
                    <form id="uncollectform" action="${ctx}/doc/unConllect" method="post">
                        <table class="table-style2 dyntable">
                            <thead>
                            <tr>
                                <th><input class="checkall" type="checkbox"/></th>
                                <th>名称</th>
                                <th>类型</th>
                                <th>收藏日期</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${docs}" var="doc">
                                <tr>
                                    <td><input name="myconllects" type="checkbox" value="${doc.id}"></td>
                                    <td>${doc.title}</td>
                                    <td>${doc.fileType}</td>
                                    <td>${doc.collectTime}</td>
                                    <td><a href="${ctx}/doc/detail/${doc.id}">查看</a>
                                        <a class="marginleft10" id="unconllect(${doc.id})"
                                           onclick="unconllect(${doc.id})" href="javascript:void(0);">取消收藏</a></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="5" rowspan="1">
                                    <a class="" href="javascript:void(0);" onclick="unconllects()">批量取消</a>
                                </td>
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
<script>
    function unconllect(id) {
        $.ajax({
            url: ctx + "/doc/uncollect",
            data: {id: id},
            success: function () {
                location.href = ctx + "/doc/myFav";
            },
            error: function () {
                layer.alert("取消关注失败");
            }
        })
    }
    function unconllects() {
        var myFavs = $("div .checked").children("input");
        if (myFavs.length == 0) {
            return;
        }
        $("#uncollectform").submit();
    }
</script>
<%@include file="../footer.jsp" %>
