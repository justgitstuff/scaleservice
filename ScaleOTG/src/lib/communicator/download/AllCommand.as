package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class AllCommand extends DownloadBase
	{
		private static var unique:AllCommand;
		public function AllCommand()
		{
			super(serverRoot+"view_control");
		}
		public function set deviceTag(value:String):void
		{
			this.HS_list.request.deviceTag=value;
		}
		public static function getInstance():AllCommand
		{
			if(unique==null)
				unique=new AllCommand();
			return unique;
		}
		
	}
}