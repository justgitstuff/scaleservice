package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class Single_Sensor extends DownloadBase
	{
		public function Single_Sensor(sensorID:uint)
		{
			//super(serverRoot+"sensor_single.xml");
			super(serverRoot+"sensor/getSensor.php");
			HS_list.request.sensorID=sensorID;
		}
		
	}
}