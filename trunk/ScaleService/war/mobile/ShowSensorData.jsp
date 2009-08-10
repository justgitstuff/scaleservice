<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="dataObject.DataType" %>
<%@ page import="dataObject.SensorData" %>
<%@ page import="java.util.List" %>
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user == null) {
%>
<p>欢迎使用ScaleExpress。您必须
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">登录</a>
后才能使用本系统</p>
<%
    } else {
%>
<p>您好<%= user.getNickname() %>!欢迎使用ScaleExpress。点击
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">此处</a>
注销</p>
<p>
</p>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择数据类型</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>

<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
      <td class="titleRow"><img src="Image/logo.png" width="48" height="48" alt="Scale Logo" /><span class="titleText">所有数据类型</span></td>
	<%
		String typeName=request.getParameter("typeName");
		DataType targetDataType=DataType.getDataType(typeName);
		if(targetDataType!=null)
		{
			List<SensorData> sensorDataList=targetDataType.getSensorData();
			for(SensorData iSensorData:sensorDataList)
			{
	%>
	<tr>
    <td class="contentRow">
    	<form action="ShowSensorData.jsp" method="post"><img src="Image/SensorDataType.png" width="24" height="24" alt="scene." /><%= iSensorData.getValue() %>
		  <br/>
          <div class="detailButton">
         	<%= iSensorData.getTime() %>
          </div>
    	</form>
    </td>
  	</tr>
	<%
			}
		}
	}
  	%>
  <tr>
    <td class="contentRow">
   	<a href="index.html">返回</a></td>
  </tr>
</table>
</body>
</html>
<?php
mysql_free_result($Recordset1);
?>
