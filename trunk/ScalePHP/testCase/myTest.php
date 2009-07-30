<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/SensorDataType.php";
include "../lib/SensorData.php";
include "../lib/Sensor.php";
$a17=SensorDataType::getUnlinkedDataType();
?>