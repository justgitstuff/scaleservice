<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Scene.php";
//$_POST["sceneID"]=3;
$result=Scene::deleteScene($_POST["sceneID"]);
buildXML($result)
?>