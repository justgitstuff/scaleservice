<?php
include "../operation/problem.php";//里面有错误常量
function insert()
{
	$result=finalExe();
	foreach($result as $insert)
	{
		global $db;
		$addFormat="call addUnitedOperation('%s','%s')";
		$query=sprintf($addFormat,$insert->lAddrID,$insert->commandID);
		mysql_query($query,$db);
		//sleep(1);
	}
}
//insert();//定义了insert函数之后，还要调用这个函数！否则根本起不了作用！
?>