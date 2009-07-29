package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class Single_Operation extends DownloadBase
	{
		private static var unique:Single_Operation;
		private var tID:String;
		private var cID:String;
		private var sID:uint;
		public function Single_Operation()
		{
			super(serverRoot+"operation/getOperation_single.php");
		}
		public function set lAddrID(value:String):void
		{
			HS_list.request.lAddrID=value;
		}
		public function set commandID(value:String):void
		{
			HS_list.request.commandID=value;
		}
		public function set sensorDataTypeID(value:uint):void
		{
			HS_list.request.sensorDataTypeID=value;
		}
		public static function getInstance():Single_Operation
		{
			if(unique==null)
				unique=new Single_Operation();
			return unique;
		}
	}
}