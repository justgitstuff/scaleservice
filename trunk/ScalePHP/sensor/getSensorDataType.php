<?php
/**
 * ����ĳһ������������SensorDataType
 * -101:Sensor������
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Sensor.php";
include "../lib/SensorDataType.php";
//$_POST["sensorID"]="2";//�������ı��
if(Sensor::isExistsSensorID($_POST["sensorID"]))
{
	buildXML(Sensor::getSensorDataType($_POST["sensorID"]));
}
else
{
	buildXML(OUTERR_SENSOR_NOT_EXIST);
}
?>