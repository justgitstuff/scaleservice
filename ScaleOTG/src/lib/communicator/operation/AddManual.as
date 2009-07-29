package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class AddManual extends CommBase
	{
		public var lAddrID:String;
		public var commandID:String;
		
		public function AddManual()
		{
			super(serverRoot+"manual/addManual.php",true);
		}
		
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.lAddrID=lAddrID;
			HS_list.request.commandID=commandID;
			super.sendHS();
		}
	}
}