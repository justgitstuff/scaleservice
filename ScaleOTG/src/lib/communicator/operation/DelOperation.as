package lib.communicator.operation
{
	import lib.communicator.CommBase;
	public class DelOperation extends CommBase
	{
		public function DelOperation()
		{
			//super(serverRoot+"EditSensor.xml",true);
			super(serverRoot+"delete_operation",true);
		}
		public function set deviceTag(value:String):void
		{
			HS_list.request.deviceTag=value;
		} 
		public function set command(value:String):void
		{
			HS_list.request.command=value;
		} 
		public function set parameter(value:String):void
		{
			HS_list.request.parameter=value;
		}
		public function set typeName(value:String):void
		{
			HS_list.request.typeName=value;
		} 
	}
}