<?php
/**
 * 添加Sensor
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Sensor.php";
//$_POST["sensorID"]="FFE110"
//$_POST["sensorName"]="Light";
//$_POST["location"]="客厅";
//$_POST["manufactor"]="Beijing";
//$_POST["description"]="Okay";
//$_POST["memo"]="No";
$result=Sensor::addSensor($_POST["sensorID"],$_POST["sensorName"],$_POST["location"],$_POST["manufactor"],$_POST["description"],$_POST["memo"]);
buildXML($result);
?>