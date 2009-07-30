<?php
class SensorData
{
	public $sensorDataID,$sensorDataTypeID,$value,$aTime;
	public function SensorData()
	{
		
	}
	/**
	 * ���Data
	 *ͨ��������Ϣ���Data
	 * 
	 * @param integer $sensorDataTypeID �������������͵�ID
	 * @param float $value ���ݵ�ֵ
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
	 * ɾ��Data
	 * ͨ��DataIDɾ��Data
	 *
	 * @param integer $sensorDataID ���ݵ�ID
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
	 * ��ѯ����Data
	 * ͨ��DataTypeID��ѯ�������ݵ���������Data
	 * 
	 * @param integer $sensorDataTypeID �������ID
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
	 * ��ѯ��n�����ݵ�
	 * ��ѯĳһDataTypeID��ĳһ���������n����¼���ö������鷵��
	 *
	 * @param integer $sensorDataTypeID �������͵�ID
	 * @param integer $dataNum ��Ҫ��ѯ�ļ�¼����
	 * @return array �������飬ÿ�����value��aTime�����ӳ�Ա
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
