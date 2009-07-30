<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Operation.php";
//$_POST["lAddrID"]='25543';
//$_POST["commandID"]='ON';
$result=Operation::getOperation_single($_POST["lAddrID"],$_POST["commandID"],$_POST["sensorDataTypeID"]);
buildXML($result);
?>