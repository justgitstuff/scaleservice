package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class AllLaddr extends DownloadBase
	{
		private static var unique:AllLaddr;
		public function AllLaddr()
		{
			//super(serverRoot+"Laddr.xml");
			super(serverRoot+"view_sensor");
		}
		public static function getInstance():AllLaddr
		{
			if(unique==null)
				unique=new AllLaddr();
			return unique;
		}
		
	}
}