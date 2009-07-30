<?php
/**
 * 返回全部传感器信息的详情
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/SensorDataType.php";
$result=SensorDataType::getSensorDataTypes();
buildXML($result);
?>