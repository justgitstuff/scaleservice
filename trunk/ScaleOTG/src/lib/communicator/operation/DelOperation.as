package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;
	public class DelOperation extends CommBase
	{
		public var lAddrID:String;
		public var commandID:String;
		public var sensorDataTypeID:uint
		public function DelOperation()
		{
			//super(serverRoot+"EditSensor.xml",true);
			super(serverRoot+"operation/deleteOperation.php",true);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.lAddrID=lAddrID;
			HS_list.request.commandID=commandID;
			HS_list.request.sensorDataTypeID=sensorDataTypeID;
			super.sendHS();
		}
	}
}