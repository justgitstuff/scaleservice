package lib.communicator
{
	import mx.collections.XMLListCollection;
	import mx.rpc.events.ResultEvent;
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
		override protected function parseHSReturn(e:ResultEvent):void
		{
			var i:uint=0;
			rcvRes=true;		
			for each(var er:XML in e.result.row)//加入序号
			{
				er.@order=i;
				i++;
			}
			this.recordXML=new XML(e.result);
			this.recordList=new XMLListCollection(e.result.row);
			super.parseHSReturn(e);
		}
		
	}
}