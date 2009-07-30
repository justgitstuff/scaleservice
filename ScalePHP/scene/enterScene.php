<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/Scene.php";
include "../operation/insertProblem.php";
//$_POST["sceneID"]=11;
$result=Scene::enterScene($_POST["sceneID"]);
insert();
buildXML($result)
?>