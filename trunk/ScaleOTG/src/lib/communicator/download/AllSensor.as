package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class AllSensor extends DownloadBase
	{
		private static var unique:AllSensor;
		public function AllSensor()
		{
			//super(serverRoot+"Sensor.xml");
			super(serverRoot+"sensor/getSensors.php");
		}
		public static function getInstance():AllSensor
		{
			if(unique==null)
				unique=new AllSensor();
			return unique;
		}
		
	}
}