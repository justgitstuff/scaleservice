<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Scene_control.php";
//$_POST["sceneID"]=1;
$result=Scene_control::getSceneControls($_POST["sceneID"]);
buildXML($result);
?>