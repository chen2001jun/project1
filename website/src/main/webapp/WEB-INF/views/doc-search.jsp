<!doctype html>
<html>
<head>
<meta charset="utf-8">
    <meta name="keywords"
          content=" fanuc/发那科/西门子/三菱数控/马扎克/华中数控/哈斯数控/CAXA/发格数控/精雕数控/海德汉数控等各类数控机床资料，比如数控操作手册，数控常用参数，数控帮助文档等"/>
    <meta name="description"
          content="在线数控文档分享平台,最专业的数控技术交流学习中心。"/>
<title>数控文档搜索 大牛数控_${param.content}</title>
<link rel="stylesheet" href="${assets}/css/public.css" type="text/css">
<link rel="stylesheet" href="${assets}/css/style.css" type="text/css">
<link rel="stylesheet" href="${assets}/css/icheck-blue.css" type="text/css">


<script type="text/javascript" src="${assets}/js/min/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${assets}/js/utils.js"></script>
<script type="text/javascript" src="${assets}/js/min/icheck.min.js"></script>
<script type="text/javascript" src="${assets}/js/min/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${assets}/js/website.js"></script>
<script type="text/javascript" src="${assets}/js/layer.js"></script>
    <script type="text/javascript" src="${assets}/js/bindAccount.js"></script>

<script type="text/javascript">
$(function(){
	$(document).on('click', '.check-toolbar .row-content .check-item', function () {
		$(this).addClass("checked").siblings().removeClass('checked');
	})
})



</script>
</head>

<body>
<div class="header">
	<div class="top">
    	<div class="container">
    		<div class="logo"></div>
            <div class="searchbar">
                <form action="${ctx}/doc/searchrecord">
                    <div class="searchbox clearfix">
                        <input id="searchKeyword" class="search-input" type="text" name="content" placeholder="请输入您要搜索的关键词" value="${param.content}">
                        <input id="searchKeywordDocType"  type="hidden" class="search-input"  name="docType" placeholder="请输入您要搜索的关键词" value="${param.docType}">
                        <input id="searchKeywordfirstClassfy"  type="hidden" class="search-input"  name="firstClassfy" placeholder="请输入您要搜索的关键词" value="${param.firstClassfy}">
                        <input id="searchKeywordsecondClassfy"  type="hidden" class="search-input"  name="secondClassfy" placeholder="请输入您要搜索的关键词" value="${param.secondClassfy}">


                        <button class="search-btn" type="submit" id="seacherText"></button>
                    </div>
                </form>
                <div class="search-hots">
                	<span>热门搜索：</span>
                    <c:forEach items="${searchWords.content}" var="searchWord">
                        <a href="${ctx}/doc/searchrecord?content=${searchWord.name}">${searchWord.name}</a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <div class="nav">
    	<div class="container">
        	<ul class="menu clearfix">
            	<li><a href="${ctx}/">首页</a></li>
                <li style="display: none">
                    <a class="icon-down" href="#"><span>分类</span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">下拉菜单一</a></li>
                        <li><a href="#">下拉菜单二</a></li>
                        <li><a href="#">下拉菜单三</a></li>
                        <li><a href="#">下拉菜单四</a></li>
                    </ul>
                </li>
            </ul>
            <c:if test="${!empty sessionScope.user}">
                <div class="nav-toolbar clearfix">
                    <div class="userbar clearfix">
                        <a class="userdrop-toggle" href="###">
                            <c:if test="${!empty sessionScope.user.avatar}">
                                <c:if test="${fn:contains(sessionScope.user.avatar, 'http') }">
                                    <img class="portrait" src="${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}">

                                </c:if>

                                <c:if test="${!fn:contains(sessionScope.user.avatar, 'http') }">
                                    <img class="portrait" src="${ctx}/${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}">

                                </c:if>
                            </c:if>

                            <c:if test="${empty sessionScope.user.avatar}">
                                <img class="portrait" src="${ctx}/${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}">

                            </c:if>
                            <span class="name">${bt:out(sessionScope.user.nickname, sessionScope.user.name)}</span>
                        </a>
                        <ul class="userbar-userdrop">
                            <div class="userbar-profile clearfix">
                                <c:if test="${!empty sessionScope.user.avatar}">
                                    <c:if test="${fn:contains(sessionScope.user.avatar, 'http') }">
                                        <img class="profile-avatar" src="${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}" alt=""/>

                                    </c:if>
                                    <c:if test="${!fn:contains(sessionScope.user.avatar, 'http') }">
                                        <img class="profile-avatar" src="${ctx}/${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}" alt=""/>

                                    </c:if>
                                </c:if>

                                <c:if test="${empty sessionScope.user.avatar}">
                                    <img class="profile-avatar" src="${ctx}/${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}" alt=""/>

                                </c:if>
                                <div class="profile-info">
                                    <div class="profile-name">
                                            ${bt:out(sessionScope.user.nickname, sessionScope.user.name)}
                                    </div>
                                    <div class="profile-score">
                                        我的积分：<em class="score">0</em>
                                    </div>
                                </div>
                            </div>
                            <div class="userbar-box">
                                <div class="userbar-menu">
                                    <a class="menu-item orange" onclick="return OBJ_bindAccount.checkBind(this)"
                                       href="${ctx}/doc/upload">
                                        <i class="fa fa-upload"></i>
                                        <div>上传文档</div>
                                    </a>
                                    <a class="menu-item blue" href="${ctx}/user/center">
                                        <i class="fa fa-desktop"></i>
                                        <div>管理中心</div>
                                    </a>
                                    <a class="menu-item dark" href="${ctx}/auth/logout">
                                        <i class="fa fa-sign-out"></i>
                                        <div>退出</div>
                                    </a>
                                </div>
                            </div>
                        </ul>
                    </div>
                </div>
            </c:if>
            <c:if test="${empty sessionScope.user}">
                <div class="nav-toolbar clearfix">
                    <ul class="iconbar clearfix">
                        <a href="${ctx}/auth/login" title="登录">
                            <li><i class="icon-login"></i> <em>登录</em></li>
                        </a>
                        <a href="${ctx}/auth/register" title="注册">
                            <li><i class="icon-user-follow"></i> <em>注册</em></li>
                        </a>
                    </ul>
                </div>
            </c:if>
        </div>
    </div>
</div>

<div class="content bg-white pd20">
	<div class="container">
    	<ul class="check-toolbar">
            <li class="clearfix">
                <div class="row-header">文档类型：</div>
                <div class="row-content clearfix" id="docTypeList">
                    <a class="check-item checked" href="searchrecord?content=${param.content}" id="docTypeAll">全部</a>
                    <a class="check-item" href="searchrecord?content=${param.content}&docType=pdf">pdf</a>
                    <a class="check-item" href="searchrecord?content=${param.content}&docType=doc">doc</a>
                    <a class="check-item" href="searchrecord?content=${param.content}&docType=xls">xls</a>
                    <a class="check-item" href="searchrecord?content=${param.content}&docType=ppt">ppt</a>
                    <a class="check-item" href="searchrecord?content=${param.content}&docType=txt">txt</a>
                </div>
            </li>
            <li class="clearfix">
                <div class="row-header">版块：</div>
                <div class="row-content clearfix" id="firstClassfyList">
                    <a class="check-item checked"   onclick="" href="${ctx}/doc/searchrecord?content=${param.content}&docType=${param.docType}&firstClassfy=" id="firstClassfyAll" >全部</a>
                    <c:forEach items="${searchCategory.content}" var="category" varStatus="i">


                    <a class="check-item" name="${category.id}" onclick="SecondCategorySeerch(${category.id});"  href="${ctx}/doc/searchrecord?content=${param.content}&docType=${param.docType}&firstClassfy=${category.id}">${category.name}</a>
                    <%--<a class="check-item" href="###">山崎马扎克</a>--%>
                    <%--<a class="check-item" href="###">西门子</a>--%>
                    <%--<a class="check-item" href="###">CAXA</a>--%>
                    <%--<a class="check-item" href="###">华中数控</a>--%>
                    <%--<a class="check-item" href="###">发那科</a>--%>
                    <%--<a class="check-item" href="###">哈斯数控</a>--%>
                    </c:forEach>
                </div>
            </li>
            <li class="fold clearfix" id="secondCargoryShow">
                <div class="row-header">分类：</div>
                <div class="row-content clearfix" id="secondCargoryList">

                    <%--<a class="check-item checked" href="###">全部</a>--%>
                    <%--<a class="check-item" href="###">说明书</a>--%>
                    <%--<a class="check-item" href="###">参考手册</a>--%>
                    <%--<a class="check-item" href="###">教程</a>--%>

                </div>
            </li>
        </ul>
        <div class="search-word" id="searchWordShow">搜索“<em>${param.content}</em>”相关的文档</div>
        
        <ul class="doc-list" id="doc-list">

            <%--<c:forEach items="${docs.content}" var="doc" varStatus="i">--%>
                <%--<dl class="clearfix">--%>
                    <%--<dt>${i.count}</dt>--%>
                    <%--<dd><a href="${ctx}/m/doc/detail?id=${doc.id}">${doc.title}</a></dd>--%>
                <%--</dl>--%>
            <%--</c:forEach>--%>

            <c:forEach items="${docs.content}" var="doc" varStatus="i">
        	<li>
            	<div class="doc-title ${doc.fileType}"><a class="linkcolor linkline" href="${ctx}/doc/detail/${doc.id}">${doc.title}</a></div>
                <c:if test="${not empty doc.summary}">
                    <div class="doc-content">${doc.summary}</div>

                </c:if>
                <div class="doc-meta clearfix">
                	<div class="meta-item">${doc.fileType}</div>
                    <div class="meta-item">阅读：${doc.views}</div>
                    <c:if test="${empty doc.user.nickname}">
                        <div class="meta-item">上传者：数控用户</div>

                    </c:if>
                    <c:if test="${not empty doc.user.nickname}">
                        <div class="meta-item">上传者：${doc.user.nickname}</div>

                    </c:if>

                    <div class="meta-item">积分：${doc.costScore}</div>
                </div>
            </li>
            </c:forEach>
        </ul>
        <!-- 分页 -->
        <div class="clearfix" id="pagingAjax" style="display: none;align-content: center" >
            <div id="pagingAjaxList" class="pagination fl margintop20 clearfix"  >
                <%--<a class="paging current" href="#">1</a>--%>
                <%--<a class="paging" href="#">2</a>--%>
                <%--<a class="paging" href="#">3</a>--%>
                <%--<a class="paging" href="#">4</a>--%>
                <%--<a class="paging" href="#">5</a>--%>
                <!-- <a class="paging ellipsis" href="javascript:void(0);">...</a> -->
                <!-- <a class="paging disable" href="#">下一页</a> -->
                <%--<a class="paging" href="#">下一页</a>--%>
            </div>
        </div>

        <%--<div id="pagingDispatcher">--%>

            <%--<div style="align-content: center;float: left">--%>
                <%--总数:   ${docs.totalElements}--%>
            <%--</div>--%>
            <%--<div>--%>
                <bt:paginationOther data="${docs}"/>
            <%--</div>--%>
        <%--</div>--%>


</div>

<div class="footer pd">
  <div class="container clearfix">
		<div class="logo"></div>
        <div class="footer-info">
        	<ul class="footer-nav clearfix">
            	<li><a class="linkcolor linkline" href="#">商务合作</a></li>
                <li><a class="linkcolor linkline" href="#">意见反馈</a></li>
                <li><a class="linkcolor linkline" href="#">关于我们</a></li>
                <li><a class="linkcolor linkline" href="#">版权声明</a></li>
            </ul>
            <div class="footer-text">
            	© Copyright 2016 蜀ICP备15032808号-1.
            </div>
        </div>
        <%--<img class="qr" src="../images/qr.jpg" width="430" height="430" alt=""/>--%>
        <img class="qr" src="${assets}/images/qr.jpg" width="430" height="430" alt=""/>
  </div>
</div>
</div>
</div>



</body>
</html>

<script type="text/javascript">





    function SecondCategorySeerch(idstr,searchWordStr){
        //var idstr=$(this).attr("name");
        //alert($(this).attr("name"));
        //alert($(this).text());
//        alert(idstr);
        //alert(searchWordStr);

        //显示分类
       // $("#secondCargoryShow").show();

        if($("#searchKeyword").val()==""&&$("#searchKeywordfirstClassfy").val()==""||$("#searchKeyword").val()==null&&$("#searchKeywordfirstClassfy").val()==null){
            $("#secondCargoryShow").hide();
        }else{
            $("#secondCargoryShow").show();
        }


       // $("#secondCargoryShow").hide();


        $.ajax({
            type:"POST",
            url:"SecondCategory",
            data:{id:idstr},
            dateType:"json",
            success:function(data){

                $("#secondCargoryList").empty();
                //$('#addFidList').append("<option value='0' selected='selected'>无父类</option>");
                $("#secondCargoryList").append("<a class=\"check-item checked\" id=\"secondClassfyAll\"   onclick=\"\" href=\"${ctx}/doc/searchrecord?content=${param.content}&docType=${param.docType}&firstClassfy=${param.firstClassfy}\" >全部</a>");

                $.each(data,function(i,item){

                    //alert(item.name);

                    //$("#secondCargoryList").append("<a onclick=\"searchSecond("+item.id+","+0+","+searchWordStr+");\" class=\"check-item\" href=\"###\">"+item.name+"</a>");
                    //$("#secondCargoryList").append("<a onclick=\"searchSecond({idstr:'"+item.id+"',pagestr:'"+0+"',searchWordStr:'"+searchWordStr+"',docTypeStr:'${param.docType}',firstClassfyStr:'${param.firstClassfy}'});\" class=\"check-item\" href=\"###\">"+item.name+"</a>");
                    $("#secondCargoryList").append("<a class=\"check-item\"  name=\""+item.id+"\"  onclick=\"\" href=\"${ctx}/doc/searchrecord?content=${param.content}&docType=${param.docType}&firstClassfy=${param.firstClassfy}&secondClassfy="+item.id+"\" >"+item.name+"</a>");


                });



            }

        });
    }


    function searchSecond(setting) {
        //alert($(this).attr("class"));



        //alert(setting.idstr);
        //alert(setting.pagestr);
        //alert(setting.searchWordStr);
        alert(setting.docTypeStr);

        alert(setting.firstClassfyStr);

        var idstr= setting.idstr;
       var pagestr=setting.pagestr;
       var searchWordStr=setting.searchWordStr;
        var docTypeStr=setting.docTypeStr;
        var firstClassfyStr=setting.firstClassfyStr;


        //动态列表
        $.ajax({
            type:"POST",
            url:"searchRecordAjax",
            data:{id:idstr,page:pagestr,content:searchWordStr,docType:docTypeStr,firstClassfy:firstClassfyStr},
            dateType:"json",
            success:function(data){

                //隐藏跳转分页
                $("#pagingDispatcher").hide();

                if(data==null||data==""){
                    //$("#pagingDispatcher").show();

                }

                //显示Ajax分页
                $("#pagingAjax").show();

                //清空文库列表
                $("#doc-list").empty();

                $("#doc-list").children().filter('li').remove();
                //$('#addFidList').append("<option value='0' selected='selected'>无父类</option>");
                $.each(data,function(i,item){



                    var fileTypeStr=item.fileType;
                    if(item.fileType=="undefined"||item.fileType==null||item.fileType==""){
                        fileTypeStr="pdf";
                    }

                    $("#doc-list").append("<li><div class=\"doc-title "+fileTypeStr+"\"><a class=\"linkcolor linkline\" href=\"${ctx}/doc/detail/"+item.id+"\">"+item.title+"</a></div>" +
                            "<div class=\"doc-content\">"+item.summary+"</div>" +
                            "<div class=\"doc-meta clearfix\">" +
                            "<div class=\"meta-item\">"+fileTypeStr+"</div>" +
                            "<div class=\"meta-item\">阅读"+item.views+"</div>" +
                            "<div class=\"meta-item\">上传者："+item.uploader+"</div>" +
                            "<div class=\"meta-item\">积分："+item.costScore+"</div></li>")


                });



            }

        });

        //动态分页


        $.ajax({
            type:"POST",
            url:"searchRecordAjaxCount",
            data:{id:idstr},
            dateType:"json",
            success:function(data) {


                $("#pagingAjaxList").empty();

                //隐藏跳转分页
                $("#pagingDispatcher").hide();
                //显示Ajax分页
                $("#pagingAjax").show();
//                alert(data.page);
//                alert(data.id)

                if (data == null || data == "") {
                    //$("#pagingAjaxList").append("总数:" + 0);
                    //$("#pagingAjaxList").append("<a  class=\"paging current\"  href=\"#\">" + 1 + "</a>");


                } else {

                    // $("#pagingAjaxList").append("总数:" + data[0].totalCount + "   ");

                }
                $.each(data,function(i,item) {
                    //alert(item.pager);

                    //$("#secondCargoryList").append("<a onclick=\"searchSecond("+item.id+");\" class=\"check-item\" href=\"###\">"+item.name+"</a>");

                    //$("#pagingAjaxList").append("<a onclick=\"searchSecond("+item.id+","+item.pager+");\" class=\"paging\"  href=\"#\">"+item.pager+"</a>");
                    var pagerStr = item.pager + 1;

                    if (item.pager == pagestr) {
                    if (pagerStr == 1) {

                    }
                        $("#pagingAjaxList").append("<a onclick=\"searchSecond({idstr:'" + item.id + "',pagestr:'" + item.pager + "',searchWordStr:'" + searchWordStr + "'});\" class=\"paging current\"  href=\"#\">" + pagerStr + "</a>");

                    } else{
                        $("#pagingAjaxList").append("<a onclick=\"searchSecond({idstr:'" + item.id + "',pagestr:'" + item.pager + "',searchWordStr:'" + searchWordStr + "'});\" class=\"paging\"  href=\"#\">" + pagerStr + "</a>");
                    }



                });



            }

        });


    }

$(function() {

    //搜索清空板块和分类搜索词
    $("#seacherText").click(function(){
        $("#searchKeywordDocType").val(null);
        $("#searchKeywordfirstClassfy").val(null);
    });


    //加载积分
    var ctx = '${ctx}';


    $.get(ctx + '/user/score', function (data) {
        if (data) {
            $('.score').html(data['totalScore']);
        }
    });


    //搜索无内容时，不显示搜索""相关文档
    if($("#searchKeyword").val()==""||$("#searchKeyword").val()==null){
          $("#searchWordShow").hide();
    }else{
          $("#searchWordShow").show();
    }


    $("#secondCargoryShow").hide();


    //alert($("#docTypeList a").val());

    //alert($("#searchKeyword").val());
    var searchWordContentStr=$("#searchKeyword").val();
    var searchWordStr=$("#searchKeywordDocType").val();
    $("#docTypeList a").each(function(){
        //var url=$(this).attr('href');
        var txt=$(this).html();
        //alert(url);
        //alert(txt);
//        alert($(this).html());
        //alert(searchWordStr);
        //$(this).attr("class","check-item checked");
//
        if(searchWordStr==txt){

            //alert($(this).html());
            $(this).attr("class","check-item checked");
            $("#docTypeAll").attr("class","check-item");
        }

    });

    var firstClassfyStr=$("#searchKeywordfirstClassfy").val();
    $("#firstClassfyList a").each(function(){
        //var url=$(this).attr('href');
        var txt=$(this).attr("name");
        //alert(url);
        //alert(txt);
//        alert($(this).html());
        //alert(searchWordStr);
        //$(this).attr("class","check-item checked");
//
        if(firstClassfyStr==txt){

            //alert($(this).html());
            $(this).attr("class","check-item checked");
            $("#firstClassfyAll").attr("class","check-item");
        }

    });







   // alert(searchWordContentStr);
    SecondCategorySeerch(firstClassfyStr,searchWordContentStr);

    var secondClassfyStr=$("#searchKeywordsecondClassfy").val();
    //alert(secondClassfyStr);

    setTimeout(function () {
        $("#secondCargoryList a").each(function(){
            //var url=$(this).attr('href');
            var txt=$(this).attr("name");


            //alert(txt);
            if(secondClassfyStr==txt){

                //alert($(this).html());
                $(this).attr("class","check-item checked");
                $("#secondClassfyAll").attr("class","check-item");
            }

        });
    },60);

});





</script>