package lib.communicator.operation
{
	import lib.communicator.CommBase;

	public class EditSensor extends CommBase
	{
		public function EditSensor(needParse:Boolean=true)
		{
			super(serverRoot+"edit_sensor",needParse);
		}
		public function set sensorTag(value:String):void
		{
			HS_list.request.sensorTag=value;
		}
		public function set sensorName(value:String):void
		{
			HS_list.request.sensorName=value;
		}
		public function set location(value:String):void
		{
			HS_list.request.location=value;
		}
		public function set manufacturer(value:String):void
		{
			HS_list.request.manufacturer=value;
		}
		public function set description(value:String):void
		{
			HS_list.request.description=value;
		}
		public function set memo(value:String):void
		{
			HS_list.request.memo=value;
		}
	}
}