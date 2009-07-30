<?php
/**
 * 返回某一次的注册结果
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/SensorDataType.php";
include "../lib/SensorData.php";
include "../lib/Sensor.php";
include "../lib/ObjToXML.php";//里面有错误常量
//$_POST["lAddrID"]='FFDDCCFFEE112233';
$result=Sensor::register($_POST["lAddrID"]);
buildXML($result);
?>