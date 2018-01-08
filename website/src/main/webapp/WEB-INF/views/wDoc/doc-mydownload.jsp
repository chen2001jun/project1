<jsp:include page="../header.jsp"/>

<div class="content pd20 management">
    <div class="container">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="doc">
            <ul class="subnav clearfix">
                <li><a href="${ctx}/doc/myUpload">我的上传</a></li>
                <li class="current"><a href="${ctx}/doc/myDownload">我的下载</a></li>
                <li><a href="${ctx}/doc/myFav">我的收藏</a></li>
            </ul>
            <div class="box clearfix">
                <div class="box-content">
                    <table class="table-style2 dyntable">
                        <thead>
                        <tr>
                            <th>名称</th>
                            <th>花费积分</th>
                            <th>类型</th>
                            <th>下载日期</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${docs}" var="doc">
                            <tr>
                                <td>${fn:escapeXml(doc.title)}</td>
                                <td>${doc.costScore}</td>
                                <td>${doc.fileType}</td>
                                <td>${doc.downloadTime}</td>
                                <td><a href="${ctx}/doc/detail/${doc.id}">查看</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/jquery.dataTables.js"></script>
<%@include file="../footer.jsp" %>