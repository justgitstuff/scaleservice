package lib.communicator.operation
{
	import lib.communicator.CommBase;

	public class RegSensorAndDataType extends CommBase
	{
		public function RegSensorAndDataType()
		{
			super(serverRoot+"register_sensorAndDataType",true);
		}
		public function set sensorTag(value:String):void
		{
			HS_list.request.sensorTag=value;
		}
		public function set typeName(value:String):void
		{
			HS_list.request.typeName=value;
		}
		public function set maxCustom(value:Number):void
		{
			HS_list.request.maxCustom=value.toString();
		}
		public function set minCustom(value:Number):void
		{
			HS_list.request.minCustom=value.toString();
		}
	}
}