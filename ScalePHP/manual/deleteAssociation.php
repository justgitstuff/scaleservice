<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Manual.php";
//$_POST["associationID"]=6;

$result=Manual::deleteAssociation($_POST["associationID"]);
buildXML($result);
?>