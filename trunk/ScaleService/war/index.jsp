<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html>
<link href="style.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="favicon.ico" />
<title>Scale业务层后台</title><body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
%>
<p>您好<%= user.getNickname() %>!欢迎使用ScaleExpress业务层后台管理系统。点击
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">此处</a>
注销</p>
<%
    } else {
%>
<p>欢迎使用ScaleExpress业务层后台管理系统。点击
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">此处</a>
登录</p>
<p>
  <%
    }
%>
</p>

<table width="100%" border="1" class="LayoutTableOutter">
  <tr>
    <td class="LayoutTable"><form id="form1" name="form1" method="post" action="/add_sensor">
  <p><strong>添加传感器</strong></p>
  <p>
    <label>传感器标识
      <input type="text" name="sensorTag" id="sensorTag" />
    </label>
  </p>
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
    <label>传感器描述
      <input type="text" name="description" id="description">
    </label>
  </p>
  <p>
    <label>传感器备注
      <input type="text" name="memo" id="memo">
    </label>
  </p>
  <p>
    <input type="submit" name="button" id="button" value="添加" />
  </p>
</form></td>
    <td class="LayoutTable"><form name="form3" method="get" action="/view_sensor">
  <p><strong>查看传感器</strong>
  </p>
  <p>
    <input type="submit" name="button3" id="button3" value="查看">
  </p>
</form></td>
  </tr>
  <tr>
    <td class="LayoutTable"><form name="form4" method="post" action="/add_dataType">
  <p><strong>添加数据类型</strong>  </p>
  <p>
    <label>传感器标识
      <input type="text" name="sensorTag" id="sensorTag">
    </label>
  </p>
  <p>
    <label>数据单位
      <input type="text" name="unit" id="unit">
    </label>
  </p>
  <p>
    <label>数据类型名
      <input type="text" name="typeName" id="typeName">
    </label>
  </p>
  <p>
    <label>最大设定值
      <input type="text" name="maxCustom" id="maxCustom">
    </label>
  </p>
  <p>
    <label>最小设定值
      <input type="text" name="minCustom" id="minCustom">
    </label>
  </p>
  <p>
    <input type="submit" name="button4" id="button4" value="添加">
  </p>
</form></td>
    <td class="LayoutTable"><form name="form2" method="get" action="/view_dataType">
  <p><strong>查看数据类型</strong>
  </p>
  <p>
    <label>传感器标识
      <input type="text" name="sensorTag" id="sensorTag">
    </label>
  </p>
  <p>
    <input type="submit" name="button2" id="button2" value="查询">
  </p>
</form></td>
  </tr>
  <tr>
    <td class="LayoutTable"><form name="form4" method="post" action="/add_sensorData">
  <p><strong>添加数据</strong>  </p>
  <p>
    <label>数据类型名
      <input type="text" name="typeName" id="typeName">
    </label>
  </p>
  <p>
    <label>值(Double型)
      <input type="text" name="value" id="value">
    </label>
  </p>
  <p>
    <input type="submit" name="button4" id="button4" value="添加">
  </p>
</form></td>
    <td class="LayoutTable"><form name="form2" method="get" action="/view_sensorData">
  <p><strong>查看数据</strong>
  </p>
  <p>
    <label>数据类型名
      <input type="text" name="typeName" id="typeName">
    </label>
  </p>
  <p>
    <input type="submit" name="button2" id="button2" value="查询">
  </p>
</form></td>
  </tr>
  
  <tr>
    <td class="LayoutTable"><form name="form4" method="post" action="/add_device">
  <p><strong>添加设备</strong>  </p>
  <p>
    <label>设备标识
      <input type="text" name="deviceTag" id="deviceTag">
    </label>
  </p>
  <p>
    <label>设备介绍
      <input type="text" name="intro" id="intro">
    </label>
  </p>
  <p>
    <input type="submit" name="button4" id="button4" value="添加">
  </p>
</form></td>
    <td class="LayoutTable"><form name="form4" method="get" action="/view_device">
  <p><strong>查看设备</strong>  </p>
  
  <p>
    <input type="submit" name="button4" id="button4" value="查看">
  </p>
</form></td>
  </tr>
  <tr>
    <td class="LayoutTable"><form name="form4" method="post" action="/add_device">
  <p><strong>添加设备</strong>  </p>
  <p>
    <label>设备标识
      <input type="text" name="deviceTag" id="deviceTag">
    </label>
  </p>
  <p>
    <label>设备介绍
      <input type="text" name="intro" id="intro">
    </label>
  </p>
  <p>
    <input type="submit" name="button4" id="button4" value="添加">
  </p>
</form></td>
    <td class="LayoutTable"><form name="form4" method="get" action="/view_device">
  <p><strong>查看设备</strong>  </p>
  
  <p>
    <input type="submit" name="button4" id="button4" value="查看">
  </p>
</form></td>
  </tr>
  <tr>
  	<td class="LayoutTable"><form name="form4" method="post" action="/add_control">
  <p><strong>添加命令</strong>  </p>
  <p>
    <label>设备标识
      <input type="text" name="deviceTag" id="deviceTag">
    </label>
  </p>
  <p>
    <label>命令内容
      <input type="text" name="command" id="command">
    </label>
  </p>
  <p>
    <label>命令参数
      <input type="text" name="parameter" id="parameter">
    </label>
  </p>
  <p>
    <label>命令说明
      <input type="text" name="action" id="action">
    </label>
  </p>
  
  <p>
    <input type="submit" name="button4" id="button4" value="添加">
  </p>
</form></td>
    <td class="LayoutTable"><form name="form4" method="get" action="/view_control">
  <p><strong>查看命令</strong>  </p>
  <p>
    <label>设备标识
      <input type="text" name="deviceTag" id="deviceTag">
    </label>
  </p>
  
  <p>
    <input type="submit" name="button4" id="button4" value="查看">
  </p>
</form></td>
  </tr>
</table>
<p>&nbsp;</p>
</body>
</html>