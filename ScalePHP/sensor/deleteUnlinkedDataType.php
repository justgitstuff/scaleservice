<?php
/**
 * ɾ��û�������������SensorDataType
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/SensorDataType.php";
$result=SensorDataType::deldeteUnlinkedDataType();
buildXML($result);
?>