package lib.communicator.operation
{
	import lib.communicator.CommBase;

	public class UnregSensor extends CommBase
	{
		public function UnregSensor()
		{
			super(serverRoot+"unregister_sensor",true);
		}
		public function set sensorTag(value:String):void
		{
			HS_list.request.sensorTag=value;
		}
	}
}