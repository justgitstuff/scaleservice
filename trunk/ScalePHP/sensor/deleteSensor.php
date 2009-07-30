<?php
/**
 * 删除Sensor
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Sensor.php";
//$_POST["sensorID"]=80;
$result=Sensor::deleteSensor($_POST["sensorID"]);
buildXML($result);
?>