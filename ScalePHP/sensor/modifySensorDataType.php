<?php
/**
 * �޸�SensorDataType
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/SensorDataType.php";
//$_POST["sensorDataTypeID"]=113;
//$_POST["typeName"]='hehe';
//$_POST["maxCustom"]=10.01;
//$_POST["minCustom"]=12.34;
$result=SensorDataType::modifySensorDataType($_POST["sensorDataTypeID"],$_POST["typeName"],$_POST["maxCustom"],$_POST["minCustom"]);
buildXML($result);
?>