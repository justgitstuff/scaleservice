package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class EditSensor extends CommBase
	{
		public var sensorID:uint;
		public var sensorName:String;
		public var location:String;
		public var manufacture:String;
		public var description:String;
		public var memo:String;
		
		public function EditSensor()
		{
			//super(serverRoot+"EditSensor.xml",true);
			super(serverRoot+"sensor/modifySensor.php",true);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.sensorID=sensorID;
			HS_list.request.sensorName=sensorName;
			HS_list.request.location=location;
			HS_list.request.manufacture=manufacture;
			HS_list.request.description=description;
			HS_list.request.memo=memo;
			super.sendHS();
		}
		
	}
}