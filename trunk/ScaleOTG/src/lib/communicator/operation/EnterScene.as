package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class EnterScene extends CommBase
	{
		public var sceneID:uint
		public function EnterScene()
		{
			super(serverRoot+'scene/enterScene.php',true);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.sceneID=sceneID;
			super.sendHS();
		}
	}
}