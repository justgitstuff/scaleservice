package lib.communicator
{
	import flash.events.Event;
	import flash.utils.ByteArray;
	
	import mx.collections.XMLListCollection;
	public class DownloadBase extends CommBase
	{
		[Bindable]
		public var recordList:XMLListCollection;
		public var recordXML:XML;
		public var rcvRes:Boolean;
		public function DownloadBase(url:String,reTry:Boolean=false)
		{
			recordList=new XMLListCollection();
			recordXML=new XML();
			rcvRes=false;
			super(url,false, reTry);
		}
		override protected function parseHSReturn(e:Event):void
		{
			var i:uint=0;
			var a:String;
			rcvRes=true;		
			
			var returnCode:int
			var bytes:ByteArray = ByteArray(e.target.data);
		    var xmlStr:String = bytes.readMultiByte(bytes.length,"gb2312");
		    this.recordXML= XML(xmlStr);
			//this.recordXML=new XML(e.target.lastResult);
			for each(var er:XML in recordXML.row)//加入序号
			{
				er.@order=i;
				i++;
			}
			this.recordList=new XMLListCollection(recordXML.row);
			super.parseHSReturn(e);
		}
		
	}
}