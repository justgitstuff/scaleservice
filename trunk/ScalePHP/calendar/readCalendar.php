<?php
set_include_path('/var/www/glib');
require_once 'Zend/Loader.php';
Zend_Loader::loadClass('Zend_Gdata');
Zend_Loader::loadClass('Zend_Gdata_AuthSub');
Zend_Loader::loadClass('Zend_Gdata_ClientLogin');
Zend_Loader::loadClass('Zend_Gdata_Calendar');
date_default_timezone_set("Asia/Shanghai");
//$_POST["user"]='everest1573@gmail.com';
//$_POST["pass"]='yj6163890622';
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
	catch(Zend_Gdata_App_Exception $ex) 
	{
		echo "Error: " . $ex->getMessage();
	}
	echo "<root>";
	foreach ($eventFeed as $event) 
	{
		$rowFormat="<row title='%s' content='%s' startTime='%s'/>";
		echo sprintf($rowFormat,$event->title,$event->content,$event->when[0]->startTime);
	}
	echo('<actionReturn>1</actionReturn>');
	echo "</root>";
}
catch(Zend_Gdata_App_AuthException $ex)
{
	echo('<root><actionReturn>-10</actionReturn></root>');
}


?>