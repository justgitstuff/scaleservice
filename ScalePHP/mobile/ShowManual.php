<?php
require_once('Connections/LocalServer.php');
include "../lib/Manual.php";
include '../operation/insertProblem.php';
?>
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
if (isset($_GET['lAddrID']) && isset($_GET['commandID'])) {
	$result=Manual::addManual($_GET["lAddrID"],$_GET["commandID"]);
	insert();
	$title=$result==1?"指令已成功执行":"指令执行失败";
}
else
{
	$title="请选择被控目标";	
}
mysql_select_db($database_LocalServer, $LocalServer);
$query_Recordset1 = "SELECT * FROM controltarget";
$Recordset1 = mysql_query($query_Recordset1, $LocalServer) or die(mysql_error());
$row_Recordset1 = mysql_fetch_assoc($Recordset1);
$totalRows_Recordset1 = mysql_num_rows($Recordset1);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>

<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <td class="titleRow"><img src="Image/logo.png" width="48" height="48" alt="Scale Logo" /><span class="titleText"><?php echo $title;?></span></td>
  <?php
  	$format='<tr>
    <td class="contentRow">
    	<form action="ShowControl.php" method="get"><img src="Image/manualCtrl.png" width="24" height="24" alt="target." />%s
		  <br/>
          <div class="detailButton">
          <input type="hidden" name="lAddrID" id="lAddrID" value="%s"/>
    	  <input type="submit" name="submit" id="submit" value="选择" />
          </div>
    	</form>
    </td>
  	</tr>
	';
	do{
		printf($format,$row_Recordset1['targetDescription'],$row_Recordset1['lAddrID']);
	}while($row_Recordset1 = mysql_fetch_assoc($Recordset1))
  ?>
  
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
