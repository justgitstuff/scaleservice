<?php
/**
 * 返回某一传感器信息的全部信息
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/SensorDataType.php";
//$_POST["sensorDataTypeID"]="113";//传感器信息的编号
$result=SensorDataType::getSensorDataTypeInfo($_POST["sensorDataTypeID"]);
buildXML($result);
?>