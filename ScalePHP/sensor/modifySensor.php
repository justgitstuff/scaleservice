<?php
/**
 * �޸�Sensor
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Sensor.php";
//$_POST["sensorID"]=47;
//$_POST["sensorName"]="Fire";
//$_POST["location"]="����";
//$_POST["manufacture"]="Beijing";
//$_POST["description"]="Okay";
//$_POST["memo"]="No";
$result=Sensor::modifySensor($_POST["sensorID"],$_POST["sensorName"],$_POST["location"],$_POST["manufacture"],$_POST["description"],$_POST["memo"]);
buildXML($result);
?>