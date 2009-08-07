package lib.factory
{
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import lib.HTTPService;
	//import mx.rpc.http.HTTPService;
	
	public class HSFactory
	{
		public function HSFactory()
		{
		}
		public static function newHS(url:String,resultParser:Function,faultParser:Function):HTTPService
		{
			var rhs:HTTPService=new HTTPService();
			rhs.url=url;
			rhs.resultFunction=resultParser;
			rhs.errorFunction=faultParser;
			return rhs;
			/*var rhs:HTTPService=new HTTPService();
			rhs.url=url;
			rhs.method="POST";
			rhs.resultFormat="e4x";
			rhs.addEventListener(ResultEvent.RESULT,resultParser);
			rhs.addEventListener(FaultEvent.FAULT,faultParser);
			return rhs;*/
		}
	}
}