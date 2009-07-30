<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/ObjToXML.php";//里面有错误常量
include "../lib/Operation.php";
//$_POST["lAddrID"]='15534';
//$_POST["commandID"]='OFF';
//$_POST["sensorDataTypeID"]=110;
//$_POST["inc"]=0;
$result=Operation::modifyOperation($_POST["lAddrID"],$_POST["commandID"],$_POST["sensorDataTypeID"],$_POST["inc"]);
buildXML($result);
?>