<?php
/**
 * ���SensorData
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/SensorData.php";
//$_POST["sensorDataTypeID"]="2";
//$_POST["value"]="10";
$result=SensorData::addData($_POST["sensorDataTypeID"],$_POST["value"]);
buildXML($result);
?>