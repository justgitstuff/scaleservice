package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class DeleteScene extends CommBase
	{
		public var sceneID:uint;
		public function DeleteScene()
		{
			super(serverRoot+"scene/deleteScene.php",true);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.sceneID=sceneID;
			super.sendHS();
		}
	}
}