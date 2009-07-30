<?php
class Operation
{
	public $lAddrID,$commandID,$sensorDataTypeID,$inc;
	public function Operation()
	{
		
	}
	public static function addOperation($lAddrID,$commandID,$sensorDataTypeID,$inc)
	{
		global $db;
		$rs=array();
		$selFromt="SELECT lAddrID,commandID FROM operation WHERE sensorDataTypeID=%d AND inc!=%d";
		$query=sprintf($selFromt,$sensorDataTypeID,$inc);
		$res=mysql_query($query,$db);
		while($resObj=mysql_fetch_object($res))
		{	
			$rs[]=$resObj;
		}
		if($rs)
		{
			foreach($rs as $temp)
			{
				$insertFormat="INSERT INTO association(lAddrID_1,commandID_1,lAddrID_2,commandID_2,direction) VALUE('%s','%s','%s','%s',0)";
				$query=sprintf($insertFormat,$lAddrID,$commandID,$temp->lAddrID,$temp->commandID);
				mysql_query($query,$db);
			}
		}
		$addFormat="call addOperation('%s','%s',%d,%d)";
		$query=sprintf($addFormat,$lAddrID,$commandID,$sensorDataTypeID,$inc);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	public static function deleteOperation($lAddrID,$commandID,$sensorDataTypeID)
	{
		global $db;
		$delFormat="call deleteOperation('%s','%s',%d)";
		$query=sprintf($delFormat,$lAddrID,$commandID,$sensorDataTypeID);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	public static function modifyOperation($lAddrID,$commandID,$sensorDataTypeID,$inc)
	{
		global $db;
		$updFormat="call modifyOperation('%s','%s',%d,%d)";
		$query=sprintf($updFormat,$lAddrID,$commandID,$sensorDataTypeID,$inc);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	public static function getOperation($sensorDataTypeID,$inc)
	{
		global $db;
		$selFormat='SELECT * FROM operation WHERE sensorDataTypeID=%d AND inc=%d';
		$query=sprintf($selFormat,$sensorDataTypeID,$inc);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			while($resObj=mysql_fetch_object($res))
				$rs[]=$resObj;
			return $rs;
		}
		else
			return ERR_SERVER_ERROR;
	}
	public static function getSensorDataType_over()
	{
		global $db;
		$selFormat='SELECT * FROM sensordatatype_over';
		$query=sprintf($selFormat);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			while($resObj=mysql_fetch_object($res))
				$rs[]=$resObj;
			return $rs;
		}
		else
			return ERR_SERVER_ERROR;
	}
	public static function getSensorDataType_below()
	{
		global $db;
		$selFormat='SELECT * FROM sensordatatype_below';
		$query=sprintf($selFormat);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			while($resObj=mysql_fetch_object($res))
				$rs[]=$resObj;
			return $rs;
		}
		else
			return ERR_SERVER_ERROR;
	}
	public static function getToDo()
	{
		global $db;
		$selFormat='SELECT * FROM todo';
		$query=sprintf($selFormat);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			while($resObj=mysql_fetch_object($res))
				$rs[]=$resObj;
			return $rs;
		}
		else
			return ERR_SERVER_ERROR;
	}
	public static function getControlTargets()
	{
		global $db;
		$selFormat='SELECT * FROM controltarget';
		$query=sprintf($selFormat);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			while($resObj=mysql_fetch_object($res))
				$rs[]=$resObj;
			return $rs;
		}
		else
			return ERR_SERVER_ERROR;
	}
	public static function getCommands()
	{
		global $db;
		$selFormat='SELECT * FROM command';
		$query=sprintf($selFormat);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			while($resObj=mysql_fetch_object($res))
				$rs[]=$resObj;
			return $rs;
		}
		else
			return ERR_SERVER_ERROR;
	}
	public static function getOperations()
	{
		global $db;
		$selFormat='SELECT * FROM getoperations';
		$query=sprintf($selFormat);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			while($resObj=mysql_fetch_object($res))
				$rs[]=$resObj;
			return $rs;
		}
		else
			return ERR_SERVER_ERROR;
	}
	public static function getOperation_single($lAddrID,$commandID,$sensorDataTypeID)
	{
		global $db;
		$selFormat="SELECT * FROM operation WHERE lAddrID='%s' AND commandID='%s' AND sensorDataTypeID=%d";
		$query=sprintf($selFormat,$lAddrID,$commandID,$sensorDataTypeID);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			while($resObj=mysql_fetch_object($res))
				$rs[]=$resObj;
			return $rs;
		}
		else
			return ERR_SERVER_ERROR;
	}
}
?>
