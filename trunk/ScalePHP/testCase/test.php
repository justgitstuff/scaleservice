<?php
include "../lib/DBLink.php";//¿Ô√Ê”–“ª∏ˆµΩ ˝æ›ø‚µƒ¡¨Ω”$db
include "../lib/Errors.php";//¿Ô√Ê”–¥ÌŒÛ≥£¡ø
include "../lib/SensorDataType.php";
include "../lib/SensorData.php";
include "../lib/Sensor.php";
$pass=true;
//≤‚ ‘addSensorDataType
$a=SensorDataType::addSensorDataType(1,'Degree','Temperature',0,0);
$b=SensorDataType::addSensorDataType(1,'Degree','Temperature',0,5);
$c=SensorDataType::addSensorDataType(1,'Degree','Temperature',5,0);
$d=SensorDataType::addSensorDataType(1,'Degree','Temperature',5,5);
$e=SensorDataType::addSensorDataType(1,'Degree');
$f=SensorDataType::addSensorDataType(1,'du','wendu',5,5);
//≤‚ ‘getInfo
$obj=new SensorDataType();
$obj->getInfo($d);
if($obj->sensorDataTypeID!=$d || $obj->sensorID!=1 || $obj->typeName!='Temperature' || $obj->unit!='Degree' || $obj->max!=5 || $obj->min!=5)
{
	echo("getInfo Test fail");
	$pass=false;
}
//≤‚ ‘getMax
if(SensorDataType::getMax($c)!=5)
{
	echo("getMax Test fail");
	$pass=false;
}
//≤‚ ‘getMin
if(SensorDataType::getMin($b)!=5)
{
	echo("getMin Test fail");
	$pass=false;
}
//≤‚ ‘modifySensorDataType
SensorDataType::modifySensorDataType($a,2,"Meter","length",3,3);
if(SensorDataType::getMax($a)!=3)
{
	echo("modifySensorDataType Test fail");
	$pass=false;
}
//≤‚ ‘isExsitsDataTypeID
$a12=SensorDatatype::isExistsSensorDataTypeID($a);
$a13=SensorDataType::isExistsSensorDataTypeID(1000);
if($a12==false || $a13==true)
{
	echo("isExistsSensorDataTypeID Test fail");
	$pass=false;
}
//≤‚ ‘getUnlinkedDataType
$a17=SensorDataType::getUnlinkedDataType();
if($a17[1]->sensorDataTypeID!=$a || $a17[4]->typeName!='Temperature')
{
	echo("getUnlinkedDataType Test fail");
	$pass=false;
}
//≤‚ ‘deleteSensorDataType
if(SensorDataType::deleteSensorDataType($a)<=0)
{
	echo("deleteSensorDataType Test fail");
	$pass=false;
}
SensorDataType::deleteSensorDataType($b);
SensorDataType::deleteSensorDataType($c);
SensorDataType::deleteSensorDataType($d);
SensorDataType::deleteSensorDataType($e);
//≤‚ ‘deldeteUnlinkedDataType
$a18=SensorDataType::deldeteUnlinkedDataType();
if($a18!=1)
{
	echo("deldeteUnlinkedDataType Test fail");
	$pass=false;
}



//≤‚ ‘addData
if(0>=$a=SensorData::addData(2,200.8))
{
	echo("addData Test fail");
	$pass=false;
}
sleep(1);
$b=SensorData::addData(2,200.9);
sleep(1);
$c=SensorData::addData(2,200.4);
sleep(1);
$d=SensorData::addData(2,200.1);
//≤‚ ‘getOverflowDataTpye
$a15=SensorDataType::getOverflowDataType();
if($a15[0]->sensorDataTypeID!=3 || $a15[1]->typeName!='Co')
{
	echo("getOverflowDataType Test fail");
	$pass=false;
}
//≤‚ ‘getLastData
$obj1=SensorData::getLastData(2);
if($obj1->value!=200.1 || !is_string($obj1->lastRenewtime))		//”–µ„Œ Ã‚◊ÓÕÌµƒƒ«Ãı «$a!?
{
	echo("getLastData Test fail");
	$pass=false;
}
//≤‚ ‘getRecentData
$obja=SensorData::getRecentData(2,4);
if($obja[1]->value!=200.4 || !is_string($obja[3]->lastRenewtime))
{
	echo("getRecentData Test fail");
	$pass=false;
}
//≤‚ ‘deleteData
SensorData::deleteData($a);
SensorData::deleteData($b);
SensorData::deleteData($c);
if(SensorData::deleteData($d)<=0)
{
	echo("deleteData Test fail");
	$pass=false;
}



//≤‚ ‘addSensor
if(0>=$a1=Sensor::addSensor('Light',100,100,100,'Hangzhou','Ok','No'))
{
	echo("addSensor Test fail");
	$pass=false;
}
$a2=Sensor::addSensor('',54,54,54,'Beijing','Okay','Yes');
$a3=Sensor::addSensor('',60,60,60);
//≤‚ ‘getInfo
$obj2=new Sensor();
$obj2->getInfo($a1);
if($obj2->sensorID!=$a1 || $obj2->sensorName!='Light' || $obj2->LocX!=100 || $obj2->LocY!=100 || $obj2->LocZ!=100 || $obj2->manufactor!='Hangzhou' || $obj2->description!='Ok' || $obj2->memo!='No')
{
	echo("getInfo Test fail");
	$pass=false;
}
//≤‚ ‘getSensors
$a14=Sensor::getSensors();
if($a14[0]->sensorName!='ZigBee Temperature'|| $a14[2]->manufactor!='Beijing')
{
	echo("getSensor Test fail");
	$pass=false;
}
//≤‚ ‘modifySensor
Sensor::modifySensor($a2,'Fire',60,60,60,'Chengdu','Good','No');
$obj3=new Sensor;
$obj3->getInfo($a2);
if($obj3->sensorName!='Fire')
{
	echo("modifySensor Test fail");
	$pass=false;
}
//≤‚ ‘getSensorDataType
$a4=SensorDataType::addSensorDataType($a1,'lex','’’∂»',10,5);
$a5=SensorDataType::addSensorDataType($a1,'mlex','π‚«ø',1000,500);
$obj4=Sensor::getSensorDataType($a1);
if($obj4[0]->unit!='lex' || $obj4[1]->sensorName!='Light' || $obj4[1]->max!='1000')
{
	echo("getSensorDataType Test fail");
	$pass=false;
}
//≤‚ ‘getLastTime
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
//≤‚ ‘isExistsSensorID
$a10=Sensor::isExistsSensorID($a1);
$a11=Sensor::isExistsSensorID(100);
if($a10==false || $a11==true)
{
	echo("isExistsSensorID Test fail");
	$pass=false;
}
//≤‚ ‘deleteSensor
Sensor::deleteSensor($a2);
Sensor::deleteSensor($a3);
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