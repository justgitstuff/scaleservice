<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/Manual.php";

include '../operation/insertProblem.php';
//$_POST["lAddrID"]='1';
//$_POST["commandID"]='1';
$result=Manual::addManual($_POST["lAddrID"],$_POST["commandID"]);
buildXML($result);
insert();

?>
