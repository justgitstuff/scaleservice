<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Scene.php";
//$_POST["sceneName"]='hehe';
//$_POST["keyWord"]='haha';
//$_POST["advMin"]=15;
$result=Scene::addScene($_POST["sceneName"],$_POST["keyWord"],$_POST["advMin"]);
buildXML($result);
?>