<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Scene_control.php";
//$_POST["sceneID"]=9;
//$_POST["lAddrID"]='15533';
//$_POST["scID"]=4;
$result=Scene_control::deleteScene_control($_POST["scID"]);
buildXML($result);
?>