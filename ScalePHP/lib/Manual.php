<?php
class Manual
{
	public $lAddrID,$commandID;
	public function Manual()
	{
		
	}
	/**
	 * 添加手动控制信息
	 * 手动添加用户信息
	 * 
	 * @param integer $sensorDataTypeID 传感器数据类型的ID
	 * @param float $value 数据的值
	 * @return integer
	 */
	public static function addManual($lAddrID,$commandID)
	{
		global $db;
		$addFormat="call addManual('%s','%s')";
		$query=sprintf($addFormat,$lAddrID,$commandID);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return SERVER_BUSY;
	}
	public static function clearManual()
	{
		global $db;
		$query="call clearManual()";
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	public static function addAssociation($lAddrID_1,$commandID_1,$lAddrID_2,$commandID_2)
	{
		global $db;
		$addFormat="call addAssociation('%s','%s','%s','%s')";
		$query=sprintf($addFormat,$lAddrID_1,$commandID_1,$lAddrID_2,$commandID_2);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	public static function deleteAssociation($associationID)
	{
		global $db;
		$addFormat="call deleteAssociation(%d)";
		$query=sprintf($addFormat,$associationID);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	public static function getAssociation($lAddrID_1,$commandID_1)
	{
		global $db;
		$selFormat='SELECT associationID,targetDescription,commandDescription FROM association JOIN controltarget ON controltarget.lAddrID=association.lAddrID_2 JOIN command ON command.commandID=association.commandID_2 WHERE lAddrID_1="%s" AND commandID_1="%s"';
		$query=sprintf($selFormat,$lAddrID_1,$commandID_1);
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