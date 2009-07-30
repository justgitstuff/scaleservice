<?php
include "../lib/ObjToXML.php";
set_include_path('/var/www/glib');
require_once 'Zend/Loader.php';
Zend_Loader::loadClass('Zend_Gdata');
Zend_Loader::loadClass('Zend_Gdata_AuthSub');
Zend_Loader::loadClass('Zend_Gdata_ClientLogin');
Zend_Loader::loadClass('Zend_Gdata_Calendar');
date_default_timezone_set("Asia/Shanghai");
//$_POST["startTime"]='2009-07-03T23:00:00.000+08:00';
//$_POST["Content"]='SUCCESSFUL';
//$_POST["user"]='everest1573@gmail.com';
//$_POST["pass"]='yj6163890622';
$result=-12;
$service=Zend_Gdata_Calendar::AUTH_SERVICE_NAME;
try
{
	$client = Zend_Gdata_ClientLogin::getHttpClient($_POST["user"], $_POST["pass"], $service);
	$service = new Zend_Gdata_Calendar($client);
	$query = $service->newEventQuery();
	$query->setUser('default');
	$query->setVisibility('private');
	$query->setProjection('full');
	$query->setOrderby('starttime');
	$query->setSortOrder('ascending');
	$query->setFutureevents('true');
	try
	{
		$eventFeed = $service->getCalendarEventFeed($query);
	}
	catch(Zend_Gdata_App_Exception $e) 
	{
	echo "Error: " . $e->getMessage();
	}
	foreach ($eventFeed as $event) 
	{
		if($event->when[0]->startTime==$_POST["startTime"])
		{
			$event->content=$service->newContent($_POST["Content"]);
	    	$event->save();
	    	if($event->content=$_POST["Content"])
	    	{
	    		$result=1;
	    	}
		}
	}
}
catch(Zend_Gdata_App_AuthException $ex)
{
	echo('<root><actionReturn>-10</actionReturn></root>');
}
buildXML($result);//修改成功$result为1,当要修改的对象不存在或者是修改失败时,$result为1!
?>
