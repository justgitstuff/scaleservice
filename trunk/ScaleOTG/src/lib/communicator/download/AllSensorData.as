package lib.communicator.download
{
	import flash.events.Event;
	
	import lib.communicator.DownloadBase;
	
	import mx.collections.Sort;
	import mx.collections.SortField;

	public class AllSensorData extends DownloadBase
	{
		private static var unique:AllSensorData;
		public function AllSensorData()
		{	
			super(serverRoot+"view_sensorData");
			//super(serverRoot+"sensorData.xml");
		}
		public static function getInstance():AllSensorData
		{
			if(unique==null)
				unique=new AllSensorData();
			return unique;
		}
		public function set typeName(value:String):void
		{
			HS_list.request.typeName=value;
		}
		override protected function parseHSReturn(e:Event):void
		{
			super.parseHSReturn(e);
			var s:Sort=new Sort();
			s.fields=[new SortField("time",false,false)];
			this.recordList.sort=s;
			this.recordList.refresh();
		}
	}
}