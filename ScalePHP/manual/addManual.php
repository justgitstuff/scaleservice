<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/Manual.php";

include '../operation/insertProblem.php';
//$_POST["lAddrID"]='1';
//$_POST["commandID"]='1';
$result=Manual::addManual($_POST["lAddrID"],$_POST["commandID"]);
buildXML($result);
insert();

?>
