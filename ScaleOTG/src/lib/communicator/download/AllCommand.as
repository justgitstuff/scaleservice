package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class AllCommand extends DownloadBase
	{
		private static var unique:AllCommand;
		public function AllCommand()
		{
			super(serverRoot+"operation/getCommands.php");
		}
		public static function getInstance():AllCommand
		{
			if(unique==null)
				unique=new AllCommand();
			return unique;
		}
		
	}
}