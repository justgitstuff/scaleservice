<?php
/**
 * ע��ĳһlAddr�����Ӧ����������������
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Sensor.php";
//$_POST["lAddrID"]=1;
$result=Sensor::lAddrUnregister($_POST["lAddrID"]);
buildXML($result);
?>