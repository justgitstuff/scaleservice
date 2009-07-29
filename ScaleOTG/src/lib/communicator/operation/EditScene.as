package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class EditScene extends CommBase
	{
		public var sceneID:uint;
		public var sceneName:String;
		public var keyWord:String;
		public var advMin:Number;
		public function EditScene()
		{
			super(serverRoot+'scene/modifyScene.php',true);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.sceneID=sceneID;
			HS_list.request.sceneName=sceneName;
			HS_list.request.keyWord=keyWord;
			HS_list.request.advMin=advMin;
			super.sendHS();
		}
	}
}