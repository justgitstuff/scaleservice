<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/SensorDataType.php";
include "../lib/SensorData.php";
include "../lib/Sensor.php";
//Sensor::receiveData('803',10,'cente');
sensor::register('803');
//Sensor::receiveData('803',10,'cente');
//Sensor::receiveData('803',10,'cente');
?>