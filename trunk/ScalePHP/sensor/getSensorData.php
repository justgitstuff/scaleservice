<?php
/**
 * ����ĳһ�������������͵Ľ�100������
 * -102:SensorDataType������
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/SensorData.php";
include "../lib/Sensor.php";
include "../lib/SensorDataType.php";
//$_POST["sensorDataTypeID"]="110";//�������������͵ı��
if(SensorDataType::isExistsSensorDataTypeID($_POST["sensorDataTypeID"]))
{
	buildXML(SensorData::getRecentData($_POST["sensorDataTypeID"],20));
}
else
{
	buildXML(OUTERR_SENSORDATATYPE_NOT_EXIST);
}
?>