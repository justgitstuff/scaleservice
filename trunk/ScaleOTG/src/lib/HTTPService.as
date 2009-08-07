package lib
{
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.system.Security;
	
	public class HTTPService
	{
		public var loader:URLLoader;
		public var urlReq:URLRequest;
		public var request:URLVariables;
		private var _url:String;		
		public function HTTPService()
		{
			loader=new URLLoader();
			loader.dataFormat=URLLoaderDataFormat.BINARY;
			urlReq=new URLRequest();
			urlReq.method=URLRequestMethod.POST;
			loader.dataFormat = URLLoaderDataFormat.BINARY;
			request=new URLVariables;
		}
		public function set url(value:String):void
		{
			this._url=value;
			urlReq.url=value;
		}
		public function get url():String
		{
			return _url;
		}
		public function set resultFunction(value:Function):void
		{
			loader.addEventListener(Event.COMPLETE,value);
		}
		public function set errorFunction(value:Function):void
		{
			loader.addEventListener(IOErrorEvent.IO_ERROR,value);
		}
		public function send():void
		{
			urlReq.data=request;
			loader.load(urlReq);
		}
	}
}