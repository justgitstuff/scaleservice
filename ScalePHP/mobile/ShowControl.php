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
$query_Recordset1 = "SELECT * FROM command";
$Recordset1 = mysql_query($query_Recordset1, $LocalServer) or die(mysql_error());
$row_Recordset1 = mysql_fetch_assoc($Recordset1);
$totalRows_Recordset1 = mysql_num_rows($Recordset1);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>

<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <td class="titleRow"><img src="Image/logo.png" width="48" height="48" alt="Scale Logo" /><span class="titleText"><?php echo isset($_GET["lAddrID"])?'请选择命令':'尚未选择目标';?></span></td>
  <?php
  	$format='<tr>
    <td class="contentRow">
    	<form action="ShowManual.php" method="get"><img src="Image/manualCtrl.png" width="24" height="24" alt="Command." />%s
		  <br/>
          <div class="detailButton">
		  <input type="hidden" name="lAddrID" id="lAddrID" value="%s"/>
          <input type="hidden" name="commandID" id="commandID" value="%s"/>
    	  <input type="submit" name="submit" id="submit" value="执行" />
          </div>
    	</form>
    </td>
  	</tr>
	';
	if(isset($_GET["lAddrID"]))
	{
		do{
			printf($format,$row_Recordset1['commandDescription'],$_GET["lAddrID"],$row_Recordset1['commandID']);
		}while($row_Recordset1 = mysql_fetch_assoc($Recordset1));
	}
  ?>
  
  <tr>
    <td class="contentRow">
   	<a href="ShowManual.php">返回</a></td>
  </tr>
</table>
</body>
</html>
<?php
mysql_free_result($Recordset1);
?>
