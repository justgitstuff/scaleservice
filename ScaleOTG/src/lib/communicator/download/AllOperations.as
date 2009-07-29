package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class AllOperations extends DownloadBase
	{
		private static var unique:AllOperations;
		
		public function AllOperations()
		{
			super(serverRoot+"operation/getOperations.php");
		}
		public static function getInstance():AllOperations
		{
			if(unique==null)
				unique=new AllOperations();
			return unique;
		}
	}
}