<?php
/**
 * 返回所有sensorDataType最后一次生成数据的详情
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/SensorDataType.php";
$result=SensorDataType::getSensorDataType_lastInfo();
buildXML($result);
?>