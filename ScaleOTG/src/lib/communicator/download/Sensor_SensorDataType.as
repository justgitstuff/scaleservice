package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class Sensor_SensorDataType extends DownloadBase
	{
		private static var unique:Sensor_SensorDataType;
		private var sID:uint;
		public function Sensor_SensorDataType()
		{
			//super(serverRoot+"SensorDataType_sensor.xml");
			super(serverRoot+"sensor/getSensorDataType.php");
			sID=0;
		}
		public function set sensorID(value:uint):void
		{
			HS_list.request.sensorID=value;
			sID=value;
		}
		public function get sensorID():uint
		{
			return sID;
		}
		public static function getInstance():Sensor_SensorDataType
		{
			if(unique==null)
				unique=new Sensor_SensorDataType();
			return unique;
		}
	}
}