<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Operation.php";
//$_POST["lAddrID"]='15534';
//$_POST["commandID"]='OFF';
//$_POST["sensorDataTypeID"]=110;
//$_POST["inc"]=0;
$result=Operation::modifyOperation($_POST["lAddrID"],$_POST["commandID"],$_POST["sensorDataTypeID"],$_POST["inc"]);
buildXML($result);
?>