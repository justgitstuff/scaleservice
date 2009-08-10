<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html>
<link href="style.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" />
<title>Scale业务层后台</title>
<head>
	<meta name="verify-v1" content="peZQGgY62NLc1fwDvCA52i8mz+4zO35YK6eFWltu/Y4=" />
	<script type="text/javascript">
		var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
		document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		<script type="text/javascript">
		try {
		var pageTracker = _gat._getTracker("UA-10150219-1");
		pageTracker._trackPageview();
		} catch(err) {}
     </script>
</head><body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user == null) {
%>
<p>欢迎使用ScaleExpress业务层后台管理系统。点击
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">此处</a>
登录</p>
<%
    } else {
%>

<p>您好<%= user.getNickname() %>!欢迎使用ScaleExpress业务层后台管理系统。点击
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">此处</a>
注销</p>
<p>您可以：</p>
<p><a href="/OTG/ScaleOTG.jsp">进入标准版系统</a></p>
<p><a href="mobile/index.html">进入手机版系统</a></p>
<p><a href="#">进入演示版系统</a></p>

<%
	}
%>

</body>
</html>