package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class AddManual extends CommBase
	{
		public function AddManual()
		{
			super(serverRoot+"add_manual",true);
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
	}
}