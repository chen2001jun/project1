<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <title>500 | <spring:message code="site.name"/></title>
</head>
<body>
<h1>500! Server internal error.</h1>
<% if (System.getProperty("os.name").toLowerCase().contains("windows")) { %>
<h3>系统执行发生错误，信息描述如下：</h3>

<div>错误状态代码：${pageContext.errorData.statusCode}</div>
<div>错误发生页面：${pageContext.errorData.requestURI}</div>
<div>错误类型信息：${pageContext.exception}</div>
<div style="margin-top: 20px; padding-left: 30px;">
    <c:forEach var="trace" items="${pageContext.exception.stackTrace}">
        ${trace}<br>
    </c:forEach>
</div>
<% } %>
</body>
</html>
