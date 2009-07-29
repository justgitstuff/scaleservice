package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class AllTarget extends DownloadBase
	{
		private static var unique:AllTarget;
		public function AllTarget()
		{
			super(serverRoot+"operation/getControlTargets.php");
		}
		public static function getInstance():AllTarget
		{
			if(unique==null)
				unique=new AllTarget();
			return unique;
		}
		
	}
}