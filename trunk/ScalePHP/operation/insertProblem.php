<?php
include "../operation/problem.php";//�����д�����
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
//insert();//������insert����֮�󣬻�Ҫ���������������������������ã�
?>