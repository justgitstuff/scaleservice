package lib.events
{
	import flash.events.Event;

	public class ItemRendererEvent extends Event
	{
		public static const SHOW_DETAIL:String="showDetail";
		public static const REG_SENSOR:String="regSensor";
		public static const UNREG_SENSOR:String="unregSensor";
		public static const DELETE_ROW:String="deleteRow";
		public static const ENTER_SCENE:String="enterScene";
		public static const GET_INFO:String="getInfo";
		public var contentID:uint;
		public var contentString:String;
		public function ItemRendererEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}