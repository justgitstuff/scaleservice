<?php
Class SensorDataType
{
	public $sensorDataTypeID,$sensorID,$unit,$typeName,$maxCustom,$minCustom;
	public function SensorDataType()
	{
		
	}
	/**
	 * ��ѯ����
	 * ͨ��SensorDataTypeID�����ݿ��в�����Ӧ�ļ�¼������д���������ĳ�Ա��
	 * 
	 * @param integer $sensorDataTypeID �������͵�ID
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
	 * ��ѯ����
	 * ͨ��SensorDataTypeID�����ݿ��в��Ҹ�SensorDataType����Ӧ�����ֵ��������֮
	 * 
	 * @param integer $sensorDataTypeID �������͵�ID
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
	 * ��ѯ����
	 * ͨ��SensorDataTypeID�����ݿ��в��Ҹ�SensorDataType����Ӧ����Сֵ��������֮
	 * 
	 * @param integer $sensorDataTypeID �������͵�ID
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
	 * ɾ��Datatype
	 * ͨ��SensorDataTypeID�����ݿ���ɾ���ü�¼
	 * 
	 * @param integer $sensorDataTypeID �������͵�ID
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
	 * ���Datatype
	 * ͨ�������������Ϣ�����Datatype
	 *
	 * @param integer $sensorID ��������ID
	 * @param String $unit ��λ����
	 * @param String $typeName ��������
	 * @param float $maxCustom ���ֵ
	 * @param float $minCustom ��Сֵ
	 * @return integer ��������ID
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
	 * �޸�DataType
	 *
	 * @param integer $sensorDataTypeID �������͵�ID
	 * @param integer $sensorID ��������ID
	 * @param String $unit ��λ����
	 * @param String $typeName ��������
	 * @param float $maxCustom ���ֵ
	 * @param float $minCustom ��Сֵ
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
	 * �ж�SensorDataTypeID�Ƿ����
	 * �ж�Ŀ��SensorDataTypeID�Ƿ����
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
	 * ��ȡ��ǰ���ݳ�����ֵ��DataType
	 * ��ȡ��ǰ���ݳ�����ֵ��DataType
	 *
	 * @return array ���ض�������,�������еĵ�ǰ���ݳ�����ֵ��DataType��DataTypeID��TypeName
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
	 * ��ѯû�������������DataType
	 * ��ѯû�������������DataType
	 *
	 * @return array ���ض�������,���а���û�����ݶ��õ�DataType��DataTypeID��TypeName
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
	 * ɾ��û�������������DataType
	 * ɾ��û�������������DataType
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
	 * ��ѯ����sensorDataType��Ӧ�����һ���������ݵ�����
	 *
	 * @return array ���ض�������,��������sensorDataType��Ӧ�����һ���������ݵ�����
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
