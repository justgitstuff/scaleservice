<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/Scene.php";
include "../operation/insertProblem.php";
//$_POST["sceneID"]=11;
$result=Scene::enterScene($_POST["sceneID"]);
insert();
buildXML($result)
?>