package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class EditSensorDataType extends CommBase
	{
		public var sensorDataTypeID:uint;
		public var typeName:String;
		public var maxCustom:Number;
		public var minCustom:Number;
		public function EditSensorDataType()
		{
			super(serverRoot+"sensor/modifySensorDataType.php",true);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.sensorDataTypeID=sensorDataTypeID;
			HS_list.request.typeName=typeName;
			HS_list.request.maxCustom=maxCustom;
			HS_list.request.minCustom=minCustom;
			super.sendHS();
		}
		
	}
}