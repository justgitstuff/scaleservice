package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class DelSceneControl extends CommBase
	{
		public var scID:uint;
		public function DelSceneControl()
		{
			super(serverRoot+"scene_control/deleteScene_control.php",true);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.scID=scID;
			super.sendHS();
		}
	}
}