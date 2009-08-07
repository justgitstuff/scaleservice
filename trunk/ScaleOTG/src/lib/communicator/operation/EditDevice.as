package lib.communicator.operation
{
	import lib.communicator.CommBase;

	public class EditDevice extends CommBase
	{
		public function EditDevice()
		{
			super(serverRoot+'edit_device',true);
		}
		public function set deviceTag(value:String):void
		{
			HS_list.request.deviceTag=value;
		}
		public function set intro(value:String):void
		{
			HS_list.request.intro=value;
		}
	}
}