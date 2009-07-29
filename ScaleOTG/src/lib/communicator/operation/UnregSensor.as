package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class UnregSensor extends CommBase
	{
		public var lAddr:String;
		public function UnregSensor()
		{
			super(serverRoot+"sensor/lAddrUnregister.php",false);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.lAddrID=lAddr;
			super.sendHS(e);
		}
	}
}