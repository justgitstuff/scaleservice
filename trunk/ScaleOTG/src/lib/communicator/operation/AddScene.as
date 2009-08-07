package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class AddScene extends CommBase
	{
		public function AddScene()
		{
			super(serverRoot+"add_scene",true);
		}
		public function set sceneName(value:String):void
		{
			HS_list.request.sceneName=value;
		}
		public function set keyWord(value:String):void
		{
			HS_list.request.firstKeyWord=value;
		}
		override public function sendHS(e:Event=null):void
		{
			super.sendHS();
		}
	}
}