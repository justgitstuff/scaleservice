package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class DeleteScene extends CommBase
	{
		private var _sceneName:String;
		public function DeleteScene()
		{
			super(serverRoot+"delete_scene",true);
		}
		public function set sceneName(value:String):void
		{
			HS_list.request.sceneName=value;
		}
	}
}