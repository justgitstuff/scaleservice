<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Operation.php";
//$_POST["lAddrID"]='25543';
//$_POST["commandID"]='ON';
$result=Operation::getOperation_single($_POST["lAddrID"],$_POST["commandID"],$_POST["sensorDataTypeID"]);
buildXML($result);
?>