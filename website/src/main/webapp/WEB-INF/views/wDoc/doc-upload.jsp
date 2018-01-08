<jsp:include page="../header.jsp"/>

<div class="content pd20 management">
    <div class="container">
        <%@include file="../left.jsp" %>
        <div class="main" data-menu="doc">
            <div class="box clearfix">
                <div class="box-header">
                    <h1><a href="${ctx}/user/profile">我的上传</a> <i class="fa fa-angle-right breadcrumbs"></i> 文档上传</h1>
                </div>
                <div class="box-content">
                    <form id="page-form" class="page-form margintop15" enctype="multipart/form-data" action="${ctx}/doc/upload" method="post">
                        <c:if test="${!empty error}">
                            <div class="row">
                                <div class="field" style="color: red;">${error}</div>
                            </div>
                        </c:if>
                        <div class="row">
                            <label for="">文件上传：</label>
                            <div class="field fileGroup">
                                <input class="ui-input-default filePath" type="text" placeholder="" readonly/><a
                                    class="button btn-normal fileSelect"><i class="fa fa-search"></i></a><input
                                    class="fileInput" style="display: none" type="file" name="file">
                                <small>仅支持TXT、PDF、Office文档和图片文件</small>
                            </div>
                        </div>
                        <div class="row">
                            <label for="">标题：</label>
                            <div class="field">
                                <input class="ui-input-default input-full" type="text" name="title" maxlength="80" value="${fn:escapeXml(param.title)}"/>
                            </div>
                        </div>
                        <div class="row">
                            <label for="">下载积分：</label>
                            <div class="field">
                                <input class="ui-input-default input-full" type="number" name="costScore" value="0" maxlength="3"
                                       min="0" max="999" style="ime-mode:disabled" onkeypress="return event.keyCode>=48&&event.keyCode<=57"
                                       onpaste="return !clipboardData.getData('text').match(/\D/)" ondragenter="return false"/>
                            </div>
                        </div>
                        <div class="row">
                            <label for="cg1">上传到分类：</label>
                            <div class="field">
                                <select id="cg1" class="ui-input-default"></select>
                                <label for="cg2">-</label>
                                <select id="cg2" class="ui-input-default" name="categoryId"></select>
                            </div>
                        </div>
                        <div class="row">
                            <label for="">简介：</label>
                            <div class="field">
                                <textarea class="ui-textarea-default input-full" name="summary" maxlength="480">${fn:escapeXml(param.summary)}</textarea>
                            </div>
                        </div>
                        <div class="row">
                            <label></label>
                            <div class="field">
                                <button class="button btn-green btn-large" type="submit">确定</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/jquery.ajaxfileupload.js"></script>
<script>
    $(function () {
        var categories = [];

        var setCg2Options = function(cg1) {
            $('#cg2').empty();
            if(cg1.children) {
                cg1.children.forEach(function(c){
                    $("#cg2").append('<option value="' + c.id + '">' + c.name + '</option>');
                });
            }
        };

        $('#cg1').change(function () {
            var val = parseInt($(this).val());
            categories.forEach(function (c) {
                if (c.id === val) {
                    setCg2Options(c);
                    return false;
                }
            });
        });

        //上传选择栏
        $('.fileGroup .fileSelect').click(function (e) {
            var $group = $(this).parent('.fileGroup');
            $group.find('.fileInput').click().change(function () {
                $group.find('.filePath').val($(this).val());
            })
        });

        $.get(ctx + '/i/doc_categories', function (data) {
            categories = data;
            data.forEach(function (c) {
                if(c.children && c.children.length > 0)
                    $("#cg1").append('<option value="' + c.id + '">' + c.name + '</option>');
            });
            if (data[0]) {
                setCg2Options(data[0]);
            }
        });
    })
</script>
<%@include file="../footer.jsp" %>