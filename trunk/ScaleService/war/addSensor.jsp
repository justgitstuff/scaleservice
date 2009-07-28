<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html>
<body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
%>
<p>Hello, <%= user.getNickname() %>! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
    } else {
%>
<p>Hello!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
to include your name with greetings you post.</p>
<p>
  <%
    }
%>
</p>
<form id="form1" name="form1" method="post" action="/addsensor">
  <p>
    <label>传感器名称
      <input type="text" name="sensorName" id="sensorName" />
    </label>
  </p>
  <p>
    <label>传感器位置
      <input type="text" name="location" id="location" />
    </label>
  </p>
  <p>
    <label>生&nbsp;产&nbsp;厂&nbsp;家&nbsp;
      <input type="text" name="manufacturer" id="manufacturer" />
    </label>
  </p>
  <p>
    <input type="submit" name="button" id="button" value="提交" />
  </p>
</form>
<p>&nbsp;</p>
</body>
</html>