<?php 
require_once('Connections/LocalServer.php'); 
include "../lib/Scene.php";
include "../operation/insertProblem.php";
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
if (isset($_GET['sceneID'])) {
  	$result=Scene::enterScene($_GET["sceneID"])?'进入场景成功':'进入场景失败';
	insert();
}
mysql_select_db($database_LocalServer, $LocalServer);
$query_Recordset1 = "SELECT * FROM scene";
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
      <td class="titleRow"><img src="Image/logo.png" width="48" height="48" alt="Scale Logo" /><span class="titleText"><?php echo isset($result)?$result:'场景一览'; ?></span></td>
  <?php
  	$format='
	<tr>
    <td class="contentRow">
    	<form action="ShowScene.php" method="get"><img src="Image/Scene.png" width="24" height="24" alt="scene." />%s
		  <br/>
          <div class="detailButton">
          <input type="hidden" name="sceneID" id="sceneID" value="%d"/>
    	  <input type="submit" name="submit" id="submit" value="进入场景" />
          </div>
    	</form>
    </td>
  	</tr>
	';
	do{
		printf($format,$row_Recordset1['sceneName'],$row_Recordset1['sceneID']);
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
