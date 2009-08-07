package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class RegSensor extends CommBase
	{
		public function RegSensor()
		{
			super(serverRoot+"register_sensor",true);
		}
		public function set sensorTag(value:String):void
		{
			HS_list.request.sensorTag=value;
		}
		public function set typeName(value:String):void
		{
			HS_list.request.typeName=value;
		}
	}
}