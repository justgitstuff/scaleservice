<?php
/**
 * ɾ��Sensor
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Sensor.php";
//$_POST["sensorID"]=80;
$result=Sensor::deleteSensor($_POST["sensorID"]);
buildXML($result);
?>