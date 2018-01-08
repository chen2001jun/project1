<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
<link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
<link rel="stylesheet" type="text/css" href="${assets}/css/swiper.css">
<script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="${assets}/js/app.js"></script>
<title>CNC数控技术</title>
</head>

<body>
<div class="container">
	<header>
		<div class="top">
	    	<div class="menu-left">
	    		<a class="menu-item"><i class="fa fa-bars"></i></a>
	        </div>
	    	
	    	<div class="title">CNC数控技术</div>
    	</div>
    	
    	<div class="tabs clearfix">
        	<a href="${ctx}/m/">首页</a>
            <a class="current" href="${ctx}/m/doc/category">分类</a>
            <a href="${ctx}/m/user/center">我的</a>
        </div>
    </header>
    
    <div class="content bg-white clearfix">
        <div class="page">
	        <div class="section">
	        	<div class="section-content">
	        		<div class="category-list">
						<c:forEach items="${categorys}" var="category">
	        			<a class="clearfix" href="${ctx}/m/doc/category/${category.id}">
	        				<div class="category-icon"></div>
	        				<div class="category-title">${category.name}</div>
	        			</a>
						</c:forEach>
	        		</div>
	        	</div>
	        </div>
        </div>
    </div>
    
    <footer>
    </footer>
</div>
</body>
</html>
