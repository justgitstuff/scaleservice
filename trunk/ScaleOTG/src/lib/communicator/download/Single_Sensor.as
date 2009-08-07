package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class Single_Sensor extends DownloadBase
	{
		public function Single_Sensor(sensorTag:String)
		{
			//super(serverRoot+"sensor_single.xml");
			super(serverRoot+"view_sensor");
			HS_list.request.sensorTag=sensorTag;
		}
		
	}
}