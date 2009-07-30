<?php
class SensorData
{
	public $sensorDataID,$sensorDataTypeID,$value,$aTime;
	public function SensorData()
	{
		
	}
	/**
	 * 添加Data
	 *通过输入信息添加Data
	 * 
	 * @param integer $sensorDataTypeID 传感器数据类型的ID
	 * @param float $value 数据的值
	 * @return integer
	 */
	public static function addData($sensorDataTypeID,$value)
	{
		global $db;
		$addFormat="call addData(%d,%f,'%s')";
		$query=sprintf($addFormat,$sensorDataTypeID,$value,date("Y-m-d H:i:s",time()));
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 删除Data
	 * 通过DataID删除Data
	 *
	 * @param integer $sensorDataID 数据的ID
	 * @return integer
	 */
	public static function deleteData($sensorDataID)
	{
		global $db;
		$delFormat='call deleteData_dataID(%d)';
		$query=sprintf($delFormat,$sensorDataID);
		if(mysql_query($query,$db))
			if(mysql_affected_rows($db))
				return SUCCESS;
			else
				return DATA_NOT_EXIST;
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 查询最晚Data
	 * 通过DataTypeID查询该类数据的最晚生成Data
	 * 
	 * @param integer $sensorDataTypeID 数据类的ID
	 * @return integer
	 */
	public static function getLastData($sensorDataTypeID)
	{
		global $db;
		$getFormat=' SELECT value,aTime FROM sensordata WHERE sensorDataTypeID=%d AND aTime=(SELECT MAX(aTime) FROM sensordata WHERE sensorDataTypeID=%d)';
		$query=sprintf($getFormat,$sensorDataTypeID,$sensorDataTypeID);
		if($res=mysql_query($query,$db))
		{
			if($resObj=mysql_fetch_object($res))
			{
				return $resObj;
			}
			else
				return ERR_NO_RESULT;
		}
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 查询近n个数据点
	 * 查询某一DataTypeID的某一数据最近的n个记录，用对象数组返回
	 *
	 * @param integer $sensorDataTypeID 数据类型的ID
	 * @param integer $dataNum 需要查询的记录个数
	 * @return array 对象数组，每项包含value和aTime两个子成员
	 */
	public static function getRecentData($sensorDataTypeID,$dataNum)
	{
		global $db;
		$getFormat='SELECT value,aTime FROM sensordata WHERE sensorDataTypeID=%d ORDER BY aTime desc LIMIT %d';
		$query=sprintf($getFormat,$sensorDataTypeID,$dataNum);
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
