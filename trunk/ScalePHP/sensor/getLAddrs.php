<?php
/**
 * 返回所有的lAddrID
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Sensor.php";
$result=Sensor::getLAddrID();
buildXML($result);
?>