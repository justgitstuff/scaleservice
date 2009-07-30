<?php
/**
 * 删除SensorDataType
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/SensorDataType.php";
//$_POST["sensorDataType"]="114";
$result=SensorDataType::deleteSensorDataType($_POST["sensorDataType"]);
buildXMl($result);
?>