<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Manual.php";
//$_POST["lAddrID_1"]='2';
//$_POST["commandID_1"]='ON';
$result=Manual::getAssociation($_POST["lAddrID_1"],$_POST["commandID_1"]);
buildXML($result);
?>