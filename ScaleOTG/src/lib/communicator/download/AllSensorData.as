package lib.communicator.download
{
	import lib.communicator.DownloadBase;
	
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.rpc.events.ResultEvent;

	public class AllSensorData extends DownloadBase
	{
		private static var unique:AllSensorData;
		private var dID:uint;
		public function AllSensorData()
		{	
			super(serverRoot+"sensor/getSensorData.php");
			//super(serverRoot+"sensorData.xml");
		}
		public static function getInstance():AllSensorData
		{
			if(unique==null)
				unique=new AllSensorData();
			return unique;
		}
		public function set sensorDataTypeID(value:uint):void
		{
			HS_list.request.sensorDataTypeID=value;
		}
		override protected function parseHSReturn(e:ResultEvent):void
		{
			super.parseHSReturn(e);
			var s:Sort=new Sort();
			s.fields=[new SortField("@aTime",false,false)];
			this.recordList.sort=s;
			this.recordList.refresh();
		}
	}
}