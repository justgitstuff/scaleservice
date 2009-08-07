package lib.googleAPI
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.utils.ByteArray;
	
	import lib.HTTPService;
	import lib.events.HSListEvent;
	import lib.serviceObject.Calendar;
	import lib.serviceObject.CalendarEvent;
	import lib.factory.HSFactory;
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	[Event(name="listSuccess", type="lib.events.HSListEvent")]
	public class GetRecentCalendar extends EventDispatcher
	{
		private static var unique:GetRecentCalendar;
		public var calendar:Calendar=null;
		protected var HS_list:HTTPService;
		public static function getInstance():GetRecentCalendar
		{
			if(unique==null)
				unique=new GetRecentCalendar();
			return unique;
		}
		public function get calendarList():ArrayCollection
		{
			return new ArrayCollection(calendar.eventList);
		}
		public function GetRecentCalendar()
		{
			this.HS_list=HSFactory.newHS("http://www.google.com/calendar/feeds/default/private/full",parseCalendar,parseFault);
		}
		public function sendHS(e:Event=null):void
		{
			trace("向Google日历API发送请求");
			HS_list.send();
		}
		private function parseCalendar(e:Event):void
		{
			var bytes:ByteArray = ByteArray(e.target.data);
		    var xmlStr:String = bytes.readMultiByte(bytes.length,"gb2312");
		    var parseXML:XML= XML(xmlStr);
		    var events:XMLList=new XMLList(parseXML.entry);
		    var iCalendarEvent:CalendarEvent;
		    for each(var er:XML in events)
		    {
		    	iCalendarEvent=new CalendarEvent(er.title,er.content,er.when.@startTime);
		    	calendar.addEvent(iCalendarEvent);
		    }
		    dispatchEvent(new HSListEvent(HSListEvent.LIST_SUCCESS));
		}
		private function parseFault(e:Event):void
		{
			trace("业务层"+this.HS_list.url+"连接失败，放弃连接。");
			Alert.show("服务连接失败：网络连接出现故障。"+this.HS_list.url,"业务层连接故障");
		}
	}
}