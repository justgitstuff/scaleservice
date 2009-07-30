<?php
/**
 * 修改SensorDataType
 */
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/SensorDataType.php";
//$_POST["sensorDataTypeID"]=113;
//$_POST["typeName"]='hehe';
//$_POST["maxCustom"]=10.01;
//$_POST["minCustom"]=12.34;
$result=SensorDataType::modifySensorDataType($_POST["sensorDataTypeID"],$_POST["typeName"],$_POST["maxCustom"],$_POST["minCustom"]);
buildXML($result);
?>