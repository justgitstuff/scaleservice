<?php
/**
 * 返回没有数据相关联的SensorDataType
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/SensorDataType.php";
$result=SensorDataType::getUnlinkedDataType();
buildXML($result);
?>