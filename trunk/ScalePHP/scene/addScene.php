<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Scene.php";
//$_POST["sceneName"]='hehe';
//$_POST["keyWord"]='haha';
//$_POST["advMin"]=15;
$result=Scene::addScene($_POST["sceneName"],$_POST["keyWord"],$_POST["advMin"]);
buildXML($result);
?>