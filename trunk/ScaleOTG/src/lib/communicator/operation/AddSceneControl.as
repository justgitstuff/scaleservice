package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class AddSceneControl extends CommBase
	{
		public var sceneID:uint;
		public var lAddrID:String;
		public var commandID:String;
		public function AddSceneControl()
		{
			super(serverRoot+"scene_control/addScene_control.php",true);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.sceneID=sceneID;
			HS_list.request.lAddrID=lAddrID;
			HS_list.request.commandID=commandID;
			super.sendHS();
		}
	}
}