<?php
class Sensor
{
	public $sensorID,$sensorName,$location,$manufacture,$description,$memo;
	public function Sensor()
	{
		
	}
	/**
	 * ����ִ�����sensor�Ĳ���,ֻ�õ���������Ľ��
	 * ͨ���������Ϣ���Sensor
	 *
	 * @param string $sensorName ������������
	 * @param integer $LocX����������λ��
	 * @param integer $LocY����������λ��
	 * @param integer $LocZ����������λ��
	 * @param string $manufacture����������������
	 * @param string $description���������ļ��
	 * @param string $memo������
	 * @return interger ����Ľ��
	 */
	public static function addSensor($sensorName,$location,$manufacture,$description,$memo)
	{
		global $db;
		$addFormat="call addSensor('%s','%s','%s','%s','%s')";
		$query=sprintf($addFormat,$sensorName,$location,$manufacture,$description,$memo);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * ִ�����sensor�Ĳ������һ�ò����sensorID
	 * ͨ���������Ϣ���Sensor
	 *
	 * @param string $sensorName ������������
	 * @param integer $LocX����������λ��
	 * @param integer $LocY����������λ��
	 * @param integer $LocZ����������λ��
	 * @param string $manufacture����������������
	 * @param string $description���������ļ��
	 * @param string $memo������
	 * @return interger ����sensorID
	 */
	public static function addNewSensor($sensorName,$location,$manufacture,$description,$memo)
	{
		global $db;
		$addFormat="INSERT INTO sensor(sensorName,location,manufacture,description,memo) VALUES ('%s','%s','%s','%s','%s')";
		$query=sprintf($addFormat,$sensorName,$location,$manufacture,$description,$memo);	
		if(mysql_query($query,$db))
			return mysql_insert_ID($db);
		else
			return ERR_SERVER_ERROR;	
	}
	/**
	 * ɾ��Sensor
	 * ͨ��SensorIDɾ���ô�����
	 *
	 * @param integer $sensorID
	 * @return integer
	 */
	public static function deleteSensor($sensorID)
	{
		global $db;
		$delFormat='call deleteSensor(%d)';
		$query=sprintf($delFormat,$sensorID);
		if(mysql_query($query,$db))
			if(mysql_affected_rows($db))
				return SUCCESS;
			else
				return SENSOR_NOT_EXIST;
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * �޸�Sensor
	 * ͨ���������Ϣ��Sensor�����޸�
	 *
	 * @param integer $sensorID ��������ID
	 * @param string $sensorName ������������
	 * @param integer $LocX ��������λ��
	 * @param integer $LocY ��������λ��
	 * @param integer $LocZ ��������λ��
	 * @param string $manufacture ��������������
	 * @param string $description �������ļ��
	 * @param string $memo ����
	 * @return integer
	 */
	public static function modifySensor($sensorID,$sensorName,$location,$manufacture,$description,$memo)
	{
		global $db;
		$updFormat="call modifySensor(%d,'%s','%s','%s','%s','%s')";
		$query=sprintf($updFormat,$sensorID,$sensorName,$location,$manufacture,$description,$memo);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * ��ѯ����
	 * ͨ��SensorID�����ݿ��в�����Ӧ�ļ�¼������д���������ĳ�Ա��
	 *
	 * @param integer $sensorID ��������ID
	 * @return integer
	 */
	public function getSensor($sensorID)
	{
		global $db;
		$selFormat='SELECT * FROM sensor WHERE sensorID=%d';
		$query=sprintf($selFormat,$sensorID);
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
	 * ��ѯ���Sensor����Ӧ������DataType
	 * ͨ��SensorID�ҵ����������е�DataType
	 *
	 * @param integer $sensorID ��������ID
	 * @return array �������飬ÿ����Ա����sensorName��sensordatatype�е���������
	 */
	public static function getSensorDataType($sensorID)
	{
		global $db;
		$selFormat='SELECT sensorName,sensordatatype.* from sensor join sensordatatype on sensor.sensorID=sensordatatype.sensorID where sensor.sensorID=%d';
		$query=sprintf($selFormat,$sensorID);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			while($resObj=mysql_fetch_object($res))		//��ѯ�����ֻһ����ʱ��һ��Ҫ��while($resObj=mysql_fetch_object($res))��������
				$rs[]=$resObj;
			return $rs;		//�������ò�ƽ������һ���ܴ������,�ղ���˼�����ص��ǲ�ѯ�����Ľ������sensordatatype�е�������֮�⻹��sensor�е�sensorName���Ҳ���һ��������Щ�����е�����
		}					//���и�ֵ������˼���Ƿ���һ���������࣬�������ڣ���ֱ�Ӱѽ����while($resObj=mysql_fetch_object($res))ȫ����������ÿ��һ�ξ�$rs[]=$resObjһ�Σ��õ�һ���������飡����ֱ�ӽ����˶���ֵ��
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * ��ѯ���Sensor���һ���������ݵ�ʱ��
	 * ͨ��SensorID�ҵ�������������һ���������ݵ�ʱ��
	 *
	 * @param integer $sensorID
	 * @return array ���ض������飬ÿ����Ա����sensorID��sensorDataTypeID��value��aTime
	 */
	public static function getLastTime($sensorID)
	{
		global $db;
		$selFormat='SELECT sensor.sensorID,sensordatatype.sensorDataTypeID,sensordata.value,sensordata.aTime 
		from sensor 
		join sensordatatype on sensor.sensorID=sensordatatype.sensorID 
		join sensordata on sensordatatype.sensorDataTypeID=sensordata.sensorDataTypeID 
		where sensor.sensorID=%d order by aTime desc limit 1';
		$query=sprintf($selFormat,$sensorID);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			if($resObj=mysql_fetch_object($res))		
			{
				$rs[]=$resObj;
				return $rs;
			}
			else
				return ERR_NO_RESULT;		//���ﲻ֪�����õ��Ƿ���ȷ!��ERR_NO_RESULTô!?��������ԭ���Ļ����Ͻ��������if����ӣ���public static function getRecentData��
		}					
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * �ж�SensorID�Ƿ����
	 * �ж�Ŀ��SensorID�Ƿ����
	 *
	 * @param integer $sensorID
	 * @return boolean
	 */
	public static function isExistsSensorID($sensorID)
	{
		global $db;
		$selFormat='SELECT location from sensor where sensorID=%d';
		$query=sprintf($selFormat,$sensorID);
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
	 * ��ѯ����Sensor
	 *
	 * @return array ���ض�������,�������е�Sensor
	 */
	public static function getSensors()
	{
		global $db;
		$selFormat='SELECT * from sensor';
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
	public static function receiveData($lAddrID,$value,$unit)
	{
		global $db;
		$receiveFormat="call receiveData('%s',%f,'%s')";
		$query=sprintf($receiveFormat,$lAddrID,$value,$unit);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	public static function register($lAddrID)
	{
		global $db;
		$selFormat="SELECT sensorID from laddr where lAddrID='%s'";
		$query=sprintf($selFormat,$lAddrID);
		if($res=mysql_query($query,$db))
		{
			$resObj=mysql_fetch_object($res);
			if($resObj->sensorID)
			{
				return REGISTED;
			}
			else
			{
				$insertID=sensor::addNewSensor('unknown','unknown','unknown','unknown','unknown');
				$updFormat="update laddr set sensorID=%d where lAddrID='%s'";
				$query=sprintf($updFormat,$insertID,$lAddrID);
				if(mysql_query($query,$db))
					if(mysql_affected_rows($db))
						return SUCCESS;
					else
						return ERR_NO_RESULT;
				else
					return ERR_SERVER_ERROR;
			}
		}
		else
			return ERR_SERVER_ERROR;
	}
	public static function getLAddrID_sensorID_null()
	{
		global $db;
		$selFormat='select * from laddrid_sensorid_null';
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
	public static function lAddrUnregister($lAddrID)
	{
		global $db;
		$updateFromat="call lAddrUnregister('%s')";
		$query=sprintf($updateFromat,$lAddrID);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
	public static function getLAddrID()
	{
		global $db;
		$selFormat='select * from laddr';
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
	public static function getSensor_single($sensorID)
	{
		global $db;
		$selFormat='SELECT * from sensor where sensorID=%d';
		$query=sprintf($selFormat,$sensorID);
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
	public static function deleteLAddr($lAddrID)
	{
		global $db;
		$updateFromat="call deleteLAddr('%s')";
		$query=sprintf($updateFromat,$lAddrID);
		if(mysql_query($query,$db))
			return SUCCESS;
		else
			return ERR_SERVER_ERROR;
	}
}
//public static function getLastaTime��ע�ͻ��е����⡣
//�������޸���֮���ǲ�ȡĬ�ϲ����ķ�ʽ���ж��û��Ƿ�������sensorName�����������Ļ��������������ʱ���Ҫ�Ƚ�С�Ĳ�����˳���Ҿ��������ϵ��Ǹ��ļ��е�����
?>