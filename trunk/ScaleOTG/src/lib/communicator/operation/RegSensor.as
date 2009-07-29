package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class RegSensor extends CommBase
	{
		public var lAddr:String;
		public function RegSensor()
		{
			super(serverRoot+"sensor/register.php",false);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.lAddrID=lAddr;
			super.sendHS(e);
		}
		
	}
}