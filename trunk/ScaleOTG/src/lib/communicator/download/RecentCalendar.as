package lib.communicator.download
{
	import flash.events.Event;
	
	import lib.communicator.DownloadBase;

	public class RecentCalendar extends DownloadBase
	{
		private static var unique:RecentCalendar;
		public var username:String;
		public var password:String;
		public function RecentCalendar()
		{
			super(serverRoot+"calendar/readCalendar.php");
		}
		public static function getInstance():RecentCalendar
		{
			if(unique==null)
				unique=new RecentCalendar();
			return unique;
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.user=username;
			HS_list.request.pass=password;
			super.sendHS();
		}
	}
}