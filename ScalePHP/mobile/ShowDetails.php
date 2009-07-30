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
$query_controltargets = "SELECT * FROM controltarget";
$controltargets = mysql_query($query_controltargets, $LocalServer) or die(mysql_error());
$row_controltargets = mysql_fetch_assoc($controltargets);
$totalRows_controltargets = mysql_num_rows($controltargets);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看家电状况</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>

<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="titleRow"><img src="Image/logo.png" width="48" height="48" alt="Scale Logo" /><span class="titleText">查看家电状况</span></td>
  </tr>
  <?php
  	$format='
	<tr>
    <td class="contentRow">
    	<img src="Image/SensorDataType.png" width="24" height="24" alt="info." />%s:			%s
    </td>
  	</tr>';
	do{
		printf($format,$row_controltargets['targetDescription'],$row_controltargets['lastCommandID']?$row_controltargets['lastCommandID']:'Unknown');
	}while($row_controltargets = mysql_fetch_assoc($controltargets))
  ?>
  <tr>
    <td class="contentRow">
   	<a href="index.html">返回</a></td>
  </tr>
</table>
</body>
</html>
<?php
mysql_free_result($controltargets);
?>
