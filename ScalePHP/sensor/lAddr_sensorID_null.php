<?php
/**
 * 返回snesorID为空的lAddrID
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Sensor.php";
$result=Sensor::getLAddrID_sensorID_null();
buildXML($result);
?>