<?php
/**
 * ɾ��SensorDataType
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/SensorDataType.php";
//$_POST["sensorDataType"]="114";
$result=SensorDataType::deleteSensorDataType($_POST["sensorDataType"]);
buildXMl($result);
?>