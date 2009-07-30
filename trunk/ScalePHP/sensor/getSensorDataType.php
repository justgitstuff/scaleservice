<?php
/**
 * 返回某一传感器的所有SensorDataType
 * -101:Sensor不存在
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Sensor.php";
include "../lib/SensorDataType.php";
//$_POST["sensorID"]="2";//传感器的编号
if(Sensor::isExistsSensorID($_POST["sensorID"]))
{
	buildXML(Sensor::getSensorDataType($_POST["sensorID"]));
}
else
{
	buildXML(OUTERR_SENSOR_NOT_EXIST);
}
?>