package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class EnterScene extends CommBase
	{
		public function EnterScene()
		{
			super(serverRoot+'enter_scene',true);
		}
		public function set sceneName(value:String):void
		{
			HS_list.request.sceneName=value;
		}
	}
}