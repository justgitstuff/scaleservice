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

$colname_Recordset1 = "-1";
if (isset($_GET['sensorDataTypeID'])) {
  $colname_Recordset1 = $_GET['sensorDataTypeID'];
}
mysql_select_db($database_LocalServer, $LocalServer);
$query_Recordset1 = sprintf("SELECT `value`, aTime FROM sensordata WHERE sensorDataTypeID = %s ORDER BY aTime DESC LIMIT 20", GetSQLValueString($colname_Recordset1, "int"));
$Recordset1 = mysql_query($query_Recordset1, $LocalServer) or die(mysql_error());
$row_Recordset1 = mysql_fetch_assoc($Recordset1);
$totalRows_Recordset1 = mysql_num_rows($Recordset1);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>详细信息</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>

<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <td class="titleRow"><img src="Image/logo.png" width="48" height="48" alt="Scale Logo" /><span class="titleText"><?php echo $_GET['typeName']?></span></td>
  <?php
  	$format='
	<tr>
    <td class="contentRow">
    	<img src="Image/SensorDataType.png" width="24" height="24" alt="info." />%s
		<div class="detailButton">
  		%s</div>
    </td>
  </tr>';
	do{
		printf($format,$row_Recordset1['value'],$row_Recordset1['aTime']);
	}while($row_Recordset1 = mysql_fetch_assoc($Recordset1))
  ?>
  <tr>
    <td class="contentRow">
   	<a href="ShowSensorDataTypeAndInfo.php">返回</a></td>
  </tr>
</table>
</body>
</html>
<?php
mysql_free_result($Recordset1);
?>
