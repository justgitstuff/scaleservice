<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Operation.php";
//$_POST["sensorDataTypeID"]=2;
//$_POST["inc"]=0;
$result=Operation::getOperation($_POST["sensorDataTypeID"],$_POST["inc"]);
buildXML($result);
?>