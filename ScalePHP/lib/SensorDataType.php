<?php
Class SensorDataType
{
	public $sensorDataTypeID,$sensorID,$unit,$typeName,$maxCustom,$minCustom;
	public function SensorDataType()
	{
		
	}
	/**
	 * 查询对象
	 * 通过SensorDataTypeID在数据库中查找相应的记录，并填写到自身对象的成员中
	 * 
	 * @param integer $sensorDataTypeID 数据类型的ID
	 * @return integer
	 */
	public function getInfo($sensorDataTypeID)
	{
		global $db;
		$selFormat='SELECT * FROM sensordatatype WHERE sensorDataTypeID=%d';
		$query=sprintf($selFormat,$sensorDataTypeID);
		if($res=mysql_query($query,$db))
		{
			if($resObj=mysql_fetch_object($res))
			{	
				foreach($resObj as $key=>$value)
					$this->$key=$value;
				return SUCCESS;
			}
			else
				return ERR_NO_RESULT;
		}
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 查询上限
	 * 通过SensorDataTypeID在数据库中查找该SensorDataType所对应的最大值，并返回之
	 * 
	 * @param integer $sensorDataTypeID 数据类型的ID
	 * @return float
	 */
	public static function getMax($sensorDataTypeID)
	{
		global $db;
		$selFormat='SELECT maxCustom FROM sensordatatype WHERE sensorDataTypeID=%d';
		$query=sprintf($selFormat,$sensorDataTypeID);
		if($res=mysql_query($query,$db))
		{
			if($resObj=mysql_fetch_object($res))
			{	
				return $resObj->max;
			}
			else
				return ERR_NO_RESULT;
		}
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 查询下限
	 * 通过SensorDataTypeID在数据库中查找该SensorDataType所对应的最小值，并返回之
	 * 
	 * @param integer $sensorDataTypeID 数据类型的ID
	 * @return float
	 */
	public static function getMin($sensorDataTypeID)
	{
		global $db;
		$selFormat='SELECT minCustom FROM sensordatatype WHERE sensorDataTypeID=%d';
		$query=sprintf($selFormat,$sensorDataTypeID);
		if($res=mysql_query($query,$db))
		{
			if($resObj=mysql_fetch_object($res))
			{	
				return $resObj->minCustom;
			}
			else
				return ERR_NO_RESULT;
		}
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 删除Datatype
	 * 通过SensorDataTypeID在数据库中删除该记录
	 * 
	 * @param integer $sensorDataTypeID 数据类型的ID
	 * @return integer 
	 */
	public static function deleteSensorDataType($sensorDataTypeID)
	{
		global $db;
		$delFormat='call deleteSensorDataType(%d)';
		$query=sprintf($delFormat,$sensorDataTypeID);
		if(mysql_query($query,$db))
			if(mysql_affected_rows($db))
				return SUCCESS;
			else
				return TYPE_NOT_EXIST;
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 添加Datatype
	 * 通过输入所需的信息，添加Datatype
	 *
	 * @param integer $sensorID 传感器的ID
	 * @param String $unit 单位名称
	 * @param String $typeName 数据名称
	 * @param float $maxCustom 最大值
	 * @param float $minCustom 最小值
	 * @return integer 最近插入的ID
	 */
	public static function addSensorDataType($sensorID,$unit,$typeName,$maxCustom,$minCustom)
	{
		global $db;
		$addFormat="call addSensorDataType(%d,'%s','%s','%s','%s')";
		$query=sprintf($addFormat,$sensorID,$unit,$typeName,$maxCustom,$minCustom);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 修改DataType
	 *
	 * @param integer $sensorDataTypeID 数据类型的ID
	 * @param integer $sensorID 传感器的ID
	 * @param String $unit 单位名称
	 * @param String $typeName 数据名称
	 * @param float $maxCustom 最大值
	 * @param float $minCustom 最小值
	 * @return integer
	 */
	public static function modifySensorDataType($sensorDataTypeID,$typeName,$maxCustom,$minCustom)
	{
		global $db;
		$updFormat="call modifySensorDataType(%d,'%s',%f,%f)";
		$query=sprintf($updFormat,$sensorDataTypeID,$typeName,$maxCustom,$minCustom);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 判断SensorDataTypeID是否存在
	 * 判断目标SensorDataTypeID是否存在
	 *
	 * @param integer $sensorDataTypeID
	 * @return boolean
	 */
	public static function isExistsSensorDataTypeID($sensorDataTypeID)
	{
		global $db;
		$selFormat='SELECT unit from sensordatatype where sensorDataTypeID=%d';
		$query=sprintf($selFormat,$sensorDataTypeID);
		if($res=mysql_query($query,$db))
		{
			if($resObj=mysql_fetch_object($res))
			{
			return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	/**
	 * 获取当前数据超过阈值的DataType
	 * 获取当前数据超过阈值的DataType
	 *
	 * @return array 返回对象数组,包含所有的当前数据超过阈值的DataType的DataTypeID和TypeName
	 */
	public static function getOverflowDataType()
	{
		global $db;
		$selFormat='select sensordatatype.sensorDataTypeID,typeName from sensordatatype join sensordata on sensordatatype.sensorDataTypeID=sensordata.sensorDataTypeID
		where sensordata.aTime=(select maxCustom(aTime) from sensordata where sensordata.sensorDataTypeID=sensordatatype.sensorDataTypeID)
		and (sensordata.value>sensordatatype.`maxCustom` or sensordata.value<sensordatatype.`minCustom`)';
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
	/**
	 * 查询没有数据相关联的DataType
	 * 查询没有数据相关联的DataType
	 *
	 * @return array 返回对象数组,其中包含没有数据对用的DataType的DataTypeID和TypeName
	 */
	public static function getUnlinkedDataType()
	{
		global $db;
		$selFormat='select sensorDataTypeID,typeName from sensordatatype
		where not exists
		(select * from sensordata where sensorDataTypeID=sensordatatype.sensorDataTypeID)';
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
	/**
	 * 删除没有数据相关联的DataType
	 * 删除没有数据相关联的DataType
	 *
	 * @return integer
	 */
	public static function deldeteUnlinkedDataType()
	{
		global $db;
		$selFormat='call deldeteUnlinkedDataType()';
		$query=sprintf($selFormat);
		if($res=mysql_query($query,$db))
		{
			return SUCCESS;
		}
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 查询所有sensorDataType对应的最后一次生成数据的详情
	 *
	 * @return array 返回对象数组,包含所有sensorDataType对应的最后一次生成数据的详情
	 */
	public static function getSensorDataType_lastInfo()
	{
		global $db;
		$selFormat='SELECT * FROM sensordatatype_lastinfo';
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
	public static function getSensorDataTypeInfo($sensorDataTypeID)
	{
		global $db;
		$selFormat='SELECT * from sensordatatype where sensorDataTypeID=%d';
		$query=sprintf($selFormat,$sensorDataTypeID);
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
	public static function getSensorDataTypes()
	{
		global $db;
		$selFormat='SELECT * from sensordatatype';
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
}
?>
