<?php
/**
 * ����ĳһ�ε�ע����
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/SensorDataType.php";
include "../lib/SensorData.php";
include "../lib/Sensor.php";
include "../lib/ObjToXML.php";//�����д�����
//$_POST["lAddrID"]='FFDDCCFFEE112233';
$result=Sensor::register($_POST["lAddrID"]);
buildXML($result);
?>