<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Scene_control.php";
//$_POST["sceneID"]=9;
//$_POST["lAddrID"]='15533';
//$_POST["scID"]=4;
$result=Scene_control::deleteScene_control($_POST["scID"]);
buildXML($result);
?>