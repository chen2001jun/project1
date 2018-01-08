<jsp:include page="header.jsp"/>

<div class="content management pd20">
    <div class="container">
        <jsp:include page="left.jsp"/>

        <div class="main" data-menu="user">
            <div class="box clearfix" style="min-height:440px;">
                <div class="box-header">
                    <h1>个人中心</h1>
                </div>
                <div class="box-content margintop15">
                    <div class="center-info clearfix">
                        <div class="welcome">
                            <div><span class="f16">Hi</span>，${bt:out(user.nickname, user.name)}，欢迎回来！</div>
                            <div><a href="${ctx}/user/profile" onclick="return OBJ_bindAccount.checkBind(this)">资料编辑</a></div>
                        </div>
                        <div class="account">
                            <div class="">我的积分：<span class="score f16">0</span></div>
                            <div><a href="javascript:void(0);">积分说明</a></div>
                        </div>
                    </div>

                    <div class="center-section margintop15">
                        <div class="section-title">快捷管理</div>
                        <ul class="center-grid clearfix">
                            <li class="grid-upload">
                                <div class="service-name">我上传的文档</div>
                                <div class="service-info"><a href="${ctx}/doc/myUpload" onclick="return OBJ_bindAccount.checkBind(this)">查看</a></div>
                                <div class="service-info"><a class="num" href="${ctx}/doc/myUpload" onclick="return OBJ_bindAccount.checkBind(this)">${myUploadCount}</a>
                                    个上传
                                </div>
                            </li>
                            <li class="grid-download">
                                <div class="service-name">我下载的文档</div>
                                <div class="service-info"><a href="${ctx}/doc/myDownload" onclick="return OBJ_bindAccount.checkBind(this)">查看</a></div>
                                <div class="service-info"><a class="num"
                                                             href="${ctx}/doc/myDownload" onclick="return OBJ_bindAccount.checkBind(this)">${myDownloadCount}</a> 个下载
                                </div>
                            </li>
                            <li class="grid-fav">
                                <div class="service-name">我收藏的文档</div>
                                <div class="service-info"><a href="${ctx}/doc/myFav" onclick="return OBJ_bindAccount.checkBind(this)">查看</a></div>
                                <div class="service-info"><a class="num" href="${ctx}/doc/myFav" onclick="return OBJ_bindAccount.checkBind(this)">${myConllectCount}</a>
                                    个收藏
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
