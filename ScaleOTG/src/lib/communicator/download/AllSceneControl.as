package lib.communicator.download
{
	import flash.events.Event;
	
	import lib.communicator.DownloadBase;

	public class AllSceneControl extends DownloadBase
	{
		private static var unique:AllSceneControl;
		public function AllSceneControl()
		{
			super(serverRoot+"view_sceneControl");
		}
		public static function getInstance():AllSceneControl
		{
			if(unique==null)
				unique=new AllSceneControl();
			return unique;
		}
		public function set sceneName(value:String):void
		{
			HS_list.request.sceneName=value;
		}
	}
}