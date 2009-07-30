<?php
include "../lib/DBLink.php";//������һ�������ݿ������$db
include "../lib/Errors.php";//�����д�����
include "../lib/SensorDataType.php";
include "../lib/SensorData.php";
include "../lib/Sensor.php";
$pass=true;
//����addSensor
if(0>=$a1=Sensor::addSensor('Light','keting','Hangzhou','Ok','No'))
{
	echo("addSensor Test fail");
	$pass=false;
}
$a2=Sensor::addSensor('Darkness','chufang','Beijing','Okay','Yes');
//����getSensor
$obj2=new Sensor();
$obj2->getSensor($a1);
if($obj2->sensorID!=$a1 || $obj2->sensorName!='Light' || $obj2->location!='keting' || $obj2->manufactor!='Hangzhou' || $obj2->description!='Ok' || $obj2->memo!='No')
{
	echo("getInfo Test fail");
	$pass=false;
}
//����getSensors
$a14=Sensor::getSensors();
if($a14[0]->sensorName!='ZigBee Temperature')
{
	echo("getSensor Test fail");
	$pass=false;
}
//����modifySensor
Sensor::modifySensor($a2,'Fire','woshi','Chengdu','Good','No');
$obj3=new Sensor;
$obj3->getSensor($a2);
if($obj3->sensorName!='Fire')
{
	echo("modifySensor Test fail");
	$pass=false;
}
//����getSensorDataType
$a4=SensorDataType::addSensorDataType($a1,'lex','�ն�',10,5);
$a5=SensorDataType::addSensorDataType($a1,'mlex','��ǿ',1000,500);
$obj4=Sensor::getSensorDataType($a1);
if($obj4[0]->unit!='lex' || $obj4[1]->sensorName!='Light' || $obj4[1]->max!='1000')
{
	echo("getSensorDataType Test fail");
	$pass=false;
}
$a6=SensorData::addData($a4,6.5);
sleep(1);
$a7=SensorData::addData($a4,7.8);
sleep(1);
$a8=SensorData::addData($a5,600);
sleep(1);
$a9=SensorData::addData($a5,700);
$obj5=Sensor::getLastTime($a1);
if($obj5[0]->value!=700 || !is_string($obj5[0]->lastRenewtime))
{
	echo("getLastTime Test fail");
	$pass=false;
}
//����isExistsSensorID
$a10=Sensor::isExistsSensorID($a1);
$a11=Sensor::isExistsSensorID(100);
if($a10==false || $a11==true)
{
	echo("isExistsSensorID Test fail");
	$pass=false;
}
//����deleteSensor
Sensor::deleteSensor($a2);
SensorDataType::deleteSensorDataType($a4);
SensorDataType::deleteSensorDataType($a5);
SensorData::deleteData($a6);
SensorData::deleteData($a7);
SensorData::deleteData($a8);
SensorData::deleteData($a9);
if(Sensor::deleteSensor($a1)<=0)
{
	echo("deleteSensor Test fail");
	$pass=false;
}
if($pass)
	echo("PASS");
?>