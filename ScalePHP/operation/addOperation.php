<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Operation.php";
//$_POST["lAddrID"]='1234';
//$_POST["commandID"]='2134';
//$_POST["sensorDataTypeID"]=21;
//$_POST["inc"]=0;
$result=Operation::addOperation($_POST["lAddrID"],$_POST["commandID"],$_POST["sensorDataTypeID"],$_POST["inc"]);
buildXML($result);
?>