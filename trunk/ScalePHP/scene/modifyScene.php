<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Scene.php";
//$_POST["sceneID"]=5;
//$_POST["sceneName"]='vivid';
//$_POST["keyWord"]='aaa';
//$_POST["advMin"]=20;
$result=Scene::modifyScene($_POST["sceneID"],$_POST["sceneName"],$_POST["keyWord"],$_POST["advMin"]);
buildXML($result);
?>