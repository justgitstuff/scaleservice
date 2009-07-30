<?php require_once('Connections/LocalServer.php'); ?>
<?php
if (!function_exists("GetSQLValueString")) {
function GetSQLValueString($theValue, $theType, $theDefinedValue = "", $theNotDefinedValue = "") 
{
  if (PHP_VERSION < 6) {
    $theValue = get_magic_quotes_gpc() ? stripslashes($theValue) : $theValue;
  }

  $theValue = function_exists("mysql_real_escape_string") ? mysql_real_escape_string($theValue) : mysql_escape_string($theValue);

  switch ($theType) {
    case "text":
      $theValue = ($theValue != "") ? "'" . $theValue . "'" : "NULL";
      break;    
    case "long":
    case "int":
      $theValue = ($theValue != "") ? intval($theValue) : "NULL";
      break;
    case "double":
      $theValue = ($theValue != "") ? doubleval($theValue) : "NULL";
      break;
    case "date":
      $theValue = ($theValue != "") ? "'" . $theValue . "'" : "NULL";
      break;
    case "defined":
      $theValue = ($theValue != "") ? $theDefinedValue : $theNotDefinedValue;
      break;
  }
  return $theValue;
}
}

mysql_select_db($database_LocalServer, $LocalServer);
$query_SensorDataTypes = "SELECT * FROM sensordatatype_lastinfo";
$SensorDataTypes = mysql_query($query_SensorDataTypes, $LocalServer) or die(mysql_error());
$row_SensorDataTypes = mysql_fetch_assoc($SensorDataTypes);
$totalRows_SensorDataTypes = mysql_num_rows($SensorDataTypes);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查阅家庭状况</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>

<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="titleRow"><img src="Image/logo.png" width="48" height="48" alt="Scale Logo" /><span class="titleText">查阅家庭状况</span></td>
  </tr>
  <?php
  	$format='
	<tr>
    <td class="contentRow">
    	<form action="ShowSensorData.php" method="get"><img src="Image/SensorDataType.png" width="24" height="24" alt="info." />%s:%s%s
		  <br/>
          <div class="detailButton">
          <input type="hidden" name="sensorDataTypeID" id="sensorDataTypeID" value="%d"/>
		  <input type="hidden" name="typeName" id="typeName" value="%s"/>
    	  <input type="submit" name="contentID" id="contentID" value="详情" />
          </div>
    	</form>
    </td>
  	</tr>';
	do{
		printf($format,$row_SensorDataTypes['typeName'],$row_SensorDataTypes['value'],$row_SensorDataTypes['unit'],$row_SensorDataTypes['sensorDataTypeID'],$row_SensorDataTypes['typeName']);
	}while($row_SensorDataTypes = mysql_fetch_assoc($SensorDataTypes))
  ?>
  <tr>
    <td class="contentRow">
   	<a href="index.html">返回</a></td>
  </tr>
</table>
</body>
</html>
<?php
mysql_free_result($SensorDataTypes);
?>
