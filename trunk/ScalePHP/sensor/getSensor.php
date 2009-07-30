<?php
/**
 * 返回某一传感器的全部信息
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Sensor.php";
//$_POST["sensorID"]="47";//传感器的编号
$result=Sensor::getSensor_single($_POST["sensorID"]);
buildXML($result);
?>