package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class AllScene extends DownloadBase
	{
		private static var unique:AllScene;
		public function AllScene()
		{
			//super(serverRoot+"Sensor.xml");
			super(serverRoot+"scene/getScene.php");
		}
		public static function getInstance():AllScene
		{
			if(unique==null)
				unique=new AllScene();
			return unique;
		}
		
	}
}