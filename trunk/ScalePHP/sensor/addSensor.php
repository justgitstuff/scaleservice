<?php
/**
 * ���Sensor
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Sensor.php";
//$_POST["sensorID"]="FFE110"
//$_POST["sensorName"]="Light";
//$_POST["location"]="����";
//$_POST["manufactor"]="Beijing";
//$_POST["description"]="Okay";
//$_POST["memo"]="No";
$result=Sensor::addSensor($_POST["sensorID"],$_POST["sensorName"],$_POST["location"],$_POST["manufactor"],$_POST["description"],$_POST["memo"]);
buildXML($result);
?>