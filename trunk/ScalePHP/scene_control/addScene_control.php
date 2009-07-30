<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Scene_control.php";
//$_POST["sceneID"]=8;
//$_POST["lAddrID"]='15534';
//$_POST["commandID"]='ON';
$result=Scene_control::addScene_control($_POST["sceneID"],$_POST["lAddrID"],$_POST["commandID"]);
buildXML($result);
?>