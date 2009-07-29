package lib.communicator.operation
{
	import lib.communicator.CommBase;
	import flash.events.Event;
	public class EditOperation extends CommBase
	{
		public var lAddrID:String;
		public var commandID:String;
		public var sensorDataTypeID:String;
		public var inc:int;
		public function EditOperation()
		{
			//super(serverRoot+"EditSensor.xml",true);
			super(serverRoot+"operation/modifyOperation.php",true);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.lAddrID=lAddrID;
			HS_list.request.commandID=commandID;
			HS_list.request.sensorDataTypeID=sensorDataTypeID;
			HS_list.request.inc=inc;
			super.sendHS();
		}
	}
}