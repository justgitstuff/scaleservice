<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Manual.php";
/*$_POST["lAddrID_1"]='1';
$_POST["commandID_1"]='a';
$_POST["lAddrID_2"]='2';
$_POST["commandID_2"]='b';*/
$result=Manual::addAssociation($_POST["lAddrID_1"],$_POST["commandID_1"],$_POST["lAddrID_2"],$_POST["commandID_2"]);
buildXML($result);
?>