<?php
/**
 * ��������sensorDataType���һ���������ݵ�����
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/SensorDataType.php";
$result=SensorDataType::getSensorDataType_lastInfo();
buildXML($result);
?>