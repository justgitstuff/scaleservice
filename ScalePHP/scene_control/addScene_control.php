<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Scene_control.php";
//$_POST["sceneID"]=8;
//$_POST["lAddrID"]='15534';
//$_POST["commandID"]='ON';
$result=Scene_control::addScene_control($_POST["sceneID"],$_POST["lAddrID"],$_POST["commandID"]);
buildXML($result);
?>