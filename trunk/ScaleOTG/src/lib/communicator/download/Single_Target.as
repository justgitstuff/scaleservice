package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class Single_Target extends DownloadBase
	{
		public function Single_Target()
		{
			//super(serverRoot+"sensor_single.xml");
			super(serverRoot+"view_device");
			
		}
		public function set deviceTag(value:String):void
		{
			HS_list.request.deviceTag=value;
		}
	}
}