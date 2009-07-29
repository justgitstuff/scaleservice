package lib.events
{
	import flash.events.Event;

	public class HSListEvent extends Event
	{
		public static const LIST_SUCCESS:String="listSuccess";
		public static const OPERATION_SUCCESS:String="operationSuccess";
		public static const OPERATION_FAIL:String="operationFail";
		public function HSListEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}