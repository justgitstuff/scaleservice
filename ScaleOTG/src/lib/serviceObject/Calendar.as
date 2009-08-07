package lib.serviceObject
{
	public class Calendar
	{
		public var eventList:Array=new Array();
		public function Calendar()
		{
		}
		public function addEvent(event:CalendarEvent):void
		{
			eventList.push(event);
		}
	}
}