<?php
/**
 * 添加SensorDataType
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/SensorDataType.php";
//$_POST["sensorID"]="1";
//$_POST["unit"]="water";
//$_POST["typeName"]="wet";
//$_POST["max"]="13";
//$_POST["min"]="8";
$result=sensorDataType::addSensorDataType($_POST["sensorID"],$_POST["unit"],$_POST["typeName"],$_POST["max"],$_POST["min"]);
buildXML($result);
?>