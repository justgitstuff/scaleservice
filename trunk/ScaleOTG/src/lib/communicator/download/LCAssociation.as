package lib.communicator.download
{
	import flash.events.Event;
	
	import lib.communicator.DownloadBase;

	public class LCAssociation extends DownloadBase
	{
		private static var unique:LCAssociation;
		public var lAddrID_1:String;
		public var commandID_1:String;
		public function LCAssociation()
		{
			super(serverRoot+"manual/getAssociation.php");
		}
		public static function getInstance():LCAssociation
		{
			if(unique==null)
				unique=new LCAssociation();
			return unique;
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.lAddrID_1=lAddrID_1;
			HS_list.request.commandID_1=commandID_1;
			super.sendHS();
		}
	}
}