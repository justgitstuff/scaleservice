<?php
/**
 * ���SensorDataType
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/SensorDataType.php";
//$_POST["sensorID"]="1";
//$_POST["unit"]="water";
//$_POST["typeName"]="wet";
//$_POST["max"]="13";
//$_POST["min"]="8";
$result=sensorDataType::addSensorDataType($_POST["sensorID"],$_POST["unit"],$_POST["typeName"],$_POST["max"],$_POST["min"]);
buildXML($result);
?>