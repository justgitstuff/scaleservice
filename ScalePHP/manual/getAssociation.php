<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Manual.php";
//$_POST["lAddrID_1"]='2';
//$_POST["commandID_1"]='ON';
$result=Manual::getAssociation($_POST["lAddrID_1"],$_POST["commandID_1"]);
buildXML($result);
?>