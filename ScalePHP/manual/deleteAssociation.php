<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Manual.php";
//$_POST["associationID"]=6;

$result=Manual::deleteAssociation($_POST["associationID"]);
buildXML($result);
?>