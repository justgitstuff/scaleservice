<?php
class Sensor
{
	public $sensorID,$sensorName,$location,$manufacture,$description,$memo;
	public function Sensor()
	{
		
	}
	/**
	 * 单纯执行添加sensor的操作,只得到插入操作的结果
	 * 通过输入的信息添加Sensor
	 *
	 * @param string $sensorName 传感器的名字
	 * @param integer $LocX　传感器的位置
	 * @param integer $LocY　传感器的位置
	 * @param integer $LocZ　传感器的位置
	 * @param string $manufacture　传感器的生产商
	 * @param string $description　传感器的简介
	 * @param string $memo　备忘
	 * @return interger 插入的结果
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
	 * 执行添加sensor的操作并且获得插入的sensorID
	 * 通过输入的信息添加Sensor
	 *
	 * @param string $sensorName 传感器的名字
	 * @param integer $LocX　传感器的位置
	 * @param integer $LocY　传感器的位置
	 * @param integer $LocZ　传感器的位置
	 * @param string $manufacture　传感器的生产商
	 * @param string $description　传感器的简介
	 * @param string $memo　备忘
	 * @return interger 插入sensorID
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
	 * 删除Sensor
	 * 通过SensorID删除该传感器
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
	 * 修改Sensor
	 * 通过数入的信息对Sensor进行修改
	 *
	 * @param integer $sensorID 传感器的ID
	 * @param string $sensorName 传感器的名字
	 * @param integer $LocX 传感器的位置
	 * @param integer $LocY 传感器的位置
	 * @param integer $LocZ 传感器的位置
	 * @param string $manufacture 传感器的生产商
	 * @param string $description 传感器的简介
	 * @param string $memo 备忘
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
	 * 查询对象
	 * 通过SensorID在数据库中查找相应的记录，并填写到自身对象的成员中
	 *
	 * @param integer $sensorID 传感器的ID
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
	 * 查询这个Sensor所对应的所有DataType
	 * 通过SensorID找到传感器所有的DataType
	 *
	 * @param integer $sensorID 传感器的ID
	 * @return array 对象数组，每个成员包含sensorName和sensordatatype中的所有属性
	 */
	public static function getSensorDataType($sensorID)
	{
		global $db;
		$selFormat='SELECT sensorName,sensordatatype.* from sensor join sensordatatype on sensor.sensorID=sensordatatype.sensorID where sensor.sensorID=%d';
		$query=sprintf($selFormat,$sensorID);
		if($res=mysql_query($query,$db))
		{
			$rs=Array();
			while($resObj=mysql_fetch_object($res))		//查询结果不只一个的时候，一定要用while($resObj=mysql_fetch_object($res))！！！！
				$rs[]=$resObj;
			return $rs;		//这个方法貌似解决了我一个很大的问题,刚才我思考的重点是查询出来的结果除了sensordatatype中的所有列之外还有sensor中的sensorName，找不到一个具有这些所有列的类来
		}					//进行赋值，正在思考是否建立一个这样的类，但是现在，我直接把结果用while($resObj=mysql_fetch_object($res))全部读出来，每读一次就$rs[]=$resObj一次，得到一个对象数组！这样直接进行了对象赋值！
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 查询这个Sensor最后一次生成数据的时间
	 * 通过SensorID找到这个传感器最后一次生成数据的时间
	 *
	 * @param integer $sensorID
	 * @return array 返回对象数组，每个成员包含sensorID，sensorDataTypeID，value，aTime
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
				return ERR_NO_RESULT;		//这里不知道我用得是否正确!是ERR_NO_RESULT么!?而且我在原来的基础上进行了这个if的添加，见public static function getRecentData！
		}					
		else
			return ERR_SERVER_ERROR;
	}
	/**
	 * 判断SensorID是否存在
	 * 判断目标SensorID是否存在
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
	 * 查询所有Sensor
	 *
	 * @return array 返回对象数组,包含所有的Sensor
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
//public static function getLastaTime的注释还有点问题。
//我现在修改了之后还是采取默认参数的方式来判断用户是否输入了sensorName，但是这样的话，在输入参数的时候就要比较小心参数的顺序！我觉得桌面上的那个文件有点问题
?>