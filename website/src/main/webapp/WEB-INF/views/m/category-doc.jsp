<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
<link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
<script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="${assets}/js/app.js"></script>
<title>${category.name}</title>
</head>

<body>
<div class="container">
	<header>
    	<a id="prevPage" class="prev" href="${ctx}/m/doc/category">
        	<i class="fa fa-chevron-left"></i>
        </a>
        
    	<div class="menu">
        </div>
    	
    	<div class="title">${category.name}</div>
    </header>
    
    <div class="content bg-white clearfix">
        <div class="page">
	        <div class="section">
	        	<div class="section-content">
	        		<div class="doc-list">
						<c:forEach items="${docs.content}" var="doc">
							<a class="${doc.fileType} clearfix" href="${ctx}/m/doc/${doc.id}?categoryId=${doc.docCategory.fid}">
								<div class="doc-icon"></div>
								<div class="doc-content">
									<div class="doc-title">${doc.title}</div>
									<div class="doc-meta">
									<span>
										<bt:fileSize size="${doc.fileSize}"/>
									</span>
										<span>上传：${doc.user.nickname}</span>
									</div>
								</div>
							</a>
							</c:forEach>
	        		</div>
					<bt:pagination data="${docs}"/>
	        	</div>
	        </div>
        </div>
    </div>
    
    <footer>
    </footer>
</div>
</body>
</html>
