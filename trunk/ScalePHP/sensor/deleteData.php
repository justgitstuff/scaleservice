<?php
/**
 * ɾ��SensorData
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/SensorData.php";
//$_POST["sensorDataID"]="575";
$result=SensorData::deleteData($_POST["sensorDataID"]);
buildXML($result);
?>