<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Operation.php";
//$_POST["lAddrID"]='1234';
//$_POST["commandID"]='2134';
$result=Operation::deleteOperation($_POST["lAddrID"],$_POST["commandID"],$_POST["sensorDataTypeID"]);
buildXML($result);
?>