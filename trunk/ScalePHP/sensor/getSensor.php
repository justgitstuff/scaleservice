<?php
/**
 * ����ĳһ��������ȫ����Ϣ
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Sensor.php";
//$_POST["sensorID"]="47";//�������ı��
$result=Sensor::getSensor_single($_POST["sensorID"]);
buildXML($result);
?>