<?php
/**
 * 添加SensorData
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/SensorData.php";
//$_POST["sensorDataTypeID"]="2";
//$_POST["value"]="10";
$result=SensorData::addData($_POST["sensorDataTypeID"],$_POST["value"]);
buildXML($result);
?>