<?php
/**
 * 返回某一传感器数据类型的近100个数据
 * -102:SensorDataType不存在
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/SensorData.php";
include "../lib/Sensor.php";
include "../lib/SensorDataType.php";
//$_POST["sensorDataTypeID"]="110";//传感器数据类型的编号
if(SensorDataType::isExistsSensorDataTypeID($_POST["sensorDataTypeID"]))
{
	buildXML(SensorData::getRecentData($_POST["sensorDataTypeID"],20));
}
else
{
	buildXML(OUTERR_SENSORDATATYPE_NOT_EXIST);
}
?>