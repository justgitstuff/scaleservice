package lib.communicator.operation
{
	import lib.communicator.CommBase;
	public class AddOperation extends CommBase
	{
		public function AddOperation()
		{
			//super(serverRoot+"EditSensor.xml",true);
			super(serverRoot+"add_operation",true);
		}
		public function set deviceTag(value:String):void
		{
			this.HS_list.request.deviceTag=value;
		}
		public function set command(value:String):void
		{
			this.HS_list.request.command=value;
		}
		public function set parameter(value:String):void
		{
			this.HS_list.request.parameter=value;
		}
		public function set direction(value:String):void
		{
			this.HS_list.request.direction=value;
		}
		public function set typeName(value:String):void
		{
			this.HS_list.request.typeName=value;
		}
	}
}