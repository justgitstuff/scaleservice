<?php
/**
 * 删除SensorData
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/SensorData.php";
//$_POST["sensorDataID"]="575";
$result=SensorData::deleteData($_POST["sensorDataID"]);
buildXML($result);
?>