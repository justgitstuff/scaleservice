<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Scene.php";
//$_POST["sceneID"]=5;
//$_POST["sceneName"]='vivid';
//$_POST["keyWord"]='aaa';
//$_POST["advMin"]=20;
$result=Scene::modifyScene($_POST["sceneID"],$_POST["sceneName"],$_POST["keyWord"],$_POST["advMin"]);
buildXML($result);
?>