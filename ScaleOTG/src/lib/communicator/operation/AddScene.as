package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class AddScene extends CommBase
	{
		public var sceneName:String;
		public var keyWord:String;
		public var advMin:uint;
		public function AddScene()
		{
			super(serverRoot+"scene/addScene.php",true);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.sceneName=sceneName;
			HS_list.request.keyWord=keyWord;
			HS_list.request.advMin=advMin;
			super.sendHS();
		}
	}
}