<?php
/**
 * ɾ��ĳһlAddr�����Ӧ����������������
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/Sensor.php";
$result=Sensor::deleteLAddr($_POST["lAddrID"]);
buildXML($result);
?>