package lib.communicator.download
{
	import flash.events.Event;
	
	import lib.communicator.DownloadBase;

	public class AllSceneControl extends DownloadBase
	{
		private static var unique:AllSceneControl;
		public var sceneID:uint;
		public function AllSceneControl()
		{
			super(serverRoot+"scene_control/getScene_control.php");
		}
		public static function getInstance():AllSceneControl
		{
			if(unique==null)
				unique=new AllSceneControl();
			return unique;
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.sceneID=sceneID;
			super.sendHS();
		}
	}
}