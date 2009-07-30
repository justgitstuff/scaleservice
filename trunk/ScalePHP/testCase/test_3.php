<?php
include "../lib/DBLink.php";//里面有一个到数据库的连接$db
include "../lib/Errors.php";//里面有错误常量
include "../lib/SensorDataType.php";
include "../lib/SensorData.php";
include "../lib/Sensor.php";
$pass=true;
//$a1=Sensor::addSensor('Light','keting','Hangzhou','Ok','No');
Sensor::modifySensor(35,'Fire','woshi','Chengdu','Good','No');
$obj3=new Sensor;
$obj3->getSensor(35);
if($obj3->sensorName!='Fire')
{
	echo("modifySensor Test fail");
	$pass=false;
}
/*SensorDataType::addSensorDataType(35,'Degree','Temperature',0,0);
SensorDataType::addSensorDataType(35,'Degree','Temperature',0,5);
SensorDataType::addSensorDataType(35,'Degree','Temperature',5,0);
SensorDataType::addSensorDataType(35,'Degree','Temperature',5,5);
*/
$obj4=Sensor::getSensorDataType(35);
if($obj4[0]->unit!='Degree' || $obj4[1]->typeName!='Temperature' || $obj4[1]->maxCustom!=0)
{
	echo("getSensorDataType Test fail");
	$pass=false;
}
SensorData::addData(110,6.04);
sleep(1);
SensorData::addData(110,7.10);
sleep(1);
SensorData::addData(110,600);
sleep(1);
SensorData::addData(110,700);
$obj5=Sensor::getLastTime(35);
if($obj5[0]->value!=700 || !is_string($obj5[0]->aTime))
{
	echo("getLastTime Test fail");
	$pass=false;
}
/*if(Sensor::deleteSensor(35)<=0)
{
	echo("deleteSensor Test fail");
	$pass=false;
}*/
$a10=Sensor::isExistsSensorID(35);
$a11=Sensor::isExistsSensorID(100);
if($a10==false || $a11==true)
{
	echo("isExistsSensorID Test fail");
	$pass=false;
}
$a14=Sensor::getSensors();
if($a14[0]->sensorName!='Zigbee Temperature'|| $a14[2]->manufacture!='Analog Device')
{
	echo("getSensors Test fail");
	$pass=false;
}
if($pass)
	echo("PASS");
?>