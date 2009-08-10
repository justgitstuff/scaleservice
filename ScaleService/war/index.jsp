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
    <td class="LayoutTable"><form id="form1" name="form1" method="post" action="/edit_sensor">
  <p><strong>修改传感器</strong></p>
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
    <input type="submit" name="button" id="button" value="修改" />
  </p>
</form><form id="form1" name="form1" method="post" action="/register_sensorAndDataType">
  <p><strong>添加数据类型并设置关联</strong></p>
  <p>
    <label>传感器标识
      <input type="text" name="sensorTag" id="sensorTag" />
    </label>
  </p>
  <p>
    <label>数据类型名
      <input type="text" name="typeName" id="typeName">
    </label>
  </p>
  <p>
    <label>最大值
      <input type="text" name="maxCustom" id="maxCustom">
    </label>
  </p>
  <p>
    <label>最小值
      <input type="text" name="minCustom" id="minCustom">
    </label>
  </p>
  <p>
    <input type="submit" name="button" id="button" value="添加" />
  </p>
</form></td>
    <td class="LayoutTable"><form name="form3" method="post" action="/view_sensor">
  <p><strong>查看传感器</strong>
  </p>
  <p>
    <input type="submit" name="button3" id="button3" value="查看">
  </p>
</form><form name="form3" method="post" action="/view_sensor">
  <p><strong>查看特定传感器</strong>
  </p>
  <p>
    <label>传感器标识
      <input type="text" name="sensorTag" id="sensorTag" />
    </label>
  </p>
  <p>
    <input type="submit" name="button3" id="button3" value="查看">
  </p>
</form><form id="form1" name="form1" method="post" action="/delete_sensor">
  <p><strong>删除传感器</strong></p>
  <p>
    <label>传感器标识
      <input type="text" name="sensorTag" id="sensorTag" />
    </label>
  </p>
  <p>
    <input type="submit" name="button" id="button" value="删除" />
  </p>
</form><form id="form1" name="form1" method="post" action="/unregister_sensor">
  <p><strong>注销传感器</strong></p>
  <p>
    <label>传感器标识
      <input type="text" name="sensorTag" id="sensorTag" />
    </label>
  </p>
  <p>
    <input type="submit" name="button" id="button" value="注销" />
  </p>
</form>
</td>
  </tr>
  <tr>
    <td class="LayoutTable"><form name="form4" method="post" action="/add_dataType">
  <p><strong>添加数据类型</strong>  </p>
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
</form></td> <td class="LayoutTable"><form name="form4" method="post" action="/edit_dataType">
  <p><strong>修改数据类型</strong>  </p>
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
    <input type="submit" name="button4" id="button4" value="修改">
  </p>
</form></td>
    <td class="LayoutTable"><form name="form2" method="post" action="/view_dataType">
  <p><strong>查看数据类型</strong>
  </p>
  <p>
    <input type="submit" name="button2" id="button2" value="查询">
  </p>
</form>
<form name="form2" method="post" action="/view_dataType">
  <p><strong>查看特定数据类型</strong>
  </p>
  <p>
    <label>数据类型名
      <input type="text" name="typeName" id="typeName">
    </label>
  </p>
  <p>
    <input type="submit" name="button2" id="button2" value="查询">
  </p>
</form>
<form name="form2" method="post" action="/delete_dataType">
  <p><strong>删除数据类型</strong>
  </p>
  <p>
    <label>数据类型名
      <input type="text" name="typeName" id="typeName">
    </label>
  </p>
  <p>
    <input type="submit" name="button2" id="button2" value="删除">
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
</form></td> <td class="LayoutTable"><form name="form4" method="post" action="/register_sensor">
  <p><strong>为数据类型关联传感器</strong>  </p>
  <p>
    <label>传感器标识<input type="text" name="sensorTag" id="sensorTag"></label>
  </p>
  <p>
    <label>数据类型名
      <input type="text" name="typeName" id="typeName">
    </label>
  </p>
  <p>
    <input type="submit" name="button4" id="button4" value="关联">
  </p>
</form></td>
    <td class="LayoutTable"><form name="form2" method="post" action="/view_sensorData">
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
</form>
    <input type="text" name="typeName2" id="typeName2"></td>
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
 <td class="LayoutTable"><form name="form4" method="post" action="/edit_device">
  <p><strong>修改设备</strong>  </p>
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
    <input type="submit" name="button4" id="button4" value="修改">
  </p>
</form></td>
    <td class="LayoutTable"><form name="form4" method="post" action="/view_device">
  <p><strong>查看设备</strong>  </p>
  
  <p>
    <input type="submit" name="button4" id="button4" value="查看">
  </p>
</form><form name="form4" method="post" action="/view_device">
  <p><strong>查看特定设备</strong>  </p>
  <p>
    <label>设备标识
      <input type="text" name="deviceTag" id="deviceTag">
    </label>
  </p>
  <p>
    <input type="submit" name="button4" id="button4" value="查看">
  </p>
</form><form name="form4" method="post" action="/delete_device">
  <p><strong>删除设备</strong>  </p>
   <p>
    <label>设备标识
      <input type="text" name="deviceTag" id="deviceTag">
    </label>
  </p>
  <p>
    <input type="submit" name="button4" id="button4" value="删除">
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
</form></td> <td class="LayoutTable">&nbsp;</td>
    <td class="LayoutTable"><form name="form4" method="post" action="/view_control">
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
  <tr>
  	<td class="LayoutTable"><form name="form4" method="post" action="/add_operation">
  <p><strong>添加操作</strong>  </p>
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
    <label>数据类型名
      <input type="text" name="typeName" id="typeName">
    </label>
  </p>
  <p>
    <label>
      <input name="direction" type="radio" id="direction_0" value="up" checked>
      上升</label>
    <br>
    <label>
      <input type="radio" name="direction" value="down" id="direction_1">
      下降</label>
  </p>
  <p>
    <input type="submit" name="button4" id="button4" value="添加">
  </p>
</form></td> <td class="LayoutTable">&nbsp;</td>
    <td class="LayoutTable"><form name="form4" method="post" action="/view_operation">
  <p><strong>查看操作</strong>  </p>
  
  <p>
    <input type="submit" name="button4" id="button4" value="查看">
  </p>
</form>
<form name="form4" method="post" action="/delete_operation">
  <p><strong>删除操作</strong>  </p>
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
    <label>数据类型名
      <input type="text" name="typeName" id="typeName">
    </label>
  </p>
  <p>
    <input type="submit" name="button4" id="button4" value="删除">
  </p>
</form>
<form name="form4" method="post" action="/view_toDo">
  <p><strong>查看自动控制</strong>  </p>
  
  <p>
    <input type="submit" name="button4" id="button4" value="查看">
  </p>
</form></td>
  </tr>
   <tr>
  	<td class="LayoutTable"><form name="form4" method="post" action="/add_scene">
  <p><strong>添加场景</strong>  </p>
  <p>
    <label>场景名称
      <input type="text" name="sceneName" id="sceneName">
    </label>
  </p>
  <p>
    <label>关键字(限一个)
      <input type="text" name="firstKeyWord" id="firstKeyWord">
    </label>
  </p>
  <p>
    <input type="submit" name="button4" id="button4" value="添加">
  </p>
</form></td> <td class="LayoutTable"><form name="form4" method="post" action="/enter_scene">
  <p><strong>进入场景</strong></p>
  <p>
    <label>场景名称
      <input type="text" name="sceneName" id="sceneName">
    </label>
  </p>
  <p>
    <input type="submit" name="button7" id="button7" value="进入">
  </p>
</form></td>
    <td class="LayoutTable"><form name="form4" method="post" action="/view_scene">
  <p><strong>查看场景</strong></p>
  
  <p>
    <input type="submit" name="button4" id="button4" value="查看">
  </p>
</form><form name="form4" method="post" action="/delete_scene">
  <p><strong>删除场景</strong>  </p>
  <p>
    <label>场景名称
      <input type="text" name="sceneName" id="sceneName">
    </label>
  </p>
  <p>
    <input type="submit" name="button4" id="button4" value="删除">
  </p>
</form></td>
  </tr>
  <tr>
  	<td class="LayoutTable"><form name="form4" method="post" action="/add_sceneControl">
  <p><strong>添加场景的控制</strong>  </p>
  <p>
    <label>场景名称
      <input type="text" name="sceneName" id="sceneName">
    </label>
  </p>
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
    <input type="submit" name="button4" id="button4" value="添加">
  </p>
</form></td> <td class="LayoutTable"><form name="form4" method="post" action="/add_sceneKeyWord">
  <p><strong>添加场景的关键字</strong></p>
  <p>
    <label>场景名称
      <input type="text" name="sceneName" id="sceneName">
    </label>
  </p>
  <p>
    <label>关键字
      <input type="text" name="keyWord" id="keyWord">
    </label>
  </p>
  
  <p>
    <input type="submit" name="button4" id="button4" value="添加">
  </p>
</form></td>
    <td class="LayoutTable"><form name="form4" method="post" action="/view_sceneControl">
  <p><strong>查看场景的控制</strong></p>
  <p>
    <label>场景名称
      <input type="text" name="sceneName" id="sceneName">
    </label>
  </p>
  
  <p>
    <input type="submit" name="button4" id="button4" value="查看">
  </p>
</form><form name="form4" method="post" action="/delete_sceneControl">
  <p><strong>删除场景的控制</strong></p>
  <p>
    <label>场景名称
      <input type="text" name="sceneName" id="sceneName">
    </label>
  </p>
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
    <input type="submit" name="button4" id="button4" value="删除">
  </p>
</form></td>
  </tr>
  <tr>
  	<td class="LayoutTable"><form name="form4" method="post" action="/add_manual">
  	  <p><strong>添加手动控制</strong></p>
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
  	    <input type="submit" name="button5" id="button5" value="添加">
	    </p>
    </form></td> <td class="LayoutTable">&nbsp;</td>
    <td class="LayoutTable"><form name="form4" method="post" action="/view_manual">
      <p><strong>查看手动控制</strong>      </p>
      <p>
        <input type="submit" name="button6" id="button6" value="查看">
      </p>
    </form><form name="form4" method="post" action="/view_manualAndAuto">
      <p><strong>查看自动和手动控制</strong>      </p>
      <p>
        <input type="submit" name="button6" id="button6" value="查看">
      </p>
    </form></td>
  </tr>
</table>
<form name="form4" method="post" action="/CCR/up">
  <p><strong>模拟CCR上传信息</strong></p>
      <p>
      <label>用户名
        <input type="text" name="username" id="username">
      </label>
      <label>信息内容
        <input type="text" name="comString" id="comString">
        </label>
        <input type="submit" name="button6" id="button6" value="上传">
      </p>
</form>
</body>
</html>