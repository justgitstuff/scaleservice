<?php
/**
 * ����ĳһ��������Ϣ��ȫ����Ϣ
 */
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/ObjToXML.php";//�����д�����
include "../lib/SensorDataType.php";
//$_POST["sensorDataTypeID"]="113";//��������Ϣ�ı��
$result=SensorDataType::getSensorDataTypeInfo($_POST["sensorDataTypeID"]);
buildXML($result);
?>