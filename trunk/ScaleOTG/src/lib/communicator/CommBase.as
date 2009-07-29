package lib.communicator
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import lib.events.HSListEvent;
	import lib.factory.HSFactory;
	
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	[Event(name="listSuccess", type="lib.events.HSListEvent")]
	[Event(name="operationSuccess", type="lib.events.HSListEvent")]
	[Event(name="operationFail", type="lib.events.HSListEvent")]
	public class CommBase extends EventDispatcher
	{
		protected var HS_list:HTTPService;
		protected var needParse:Boolean;
		//public static var serverRoot:String="http://192.168.1.100/ScaleService/";
		//public static var serverRoot:String="http://192.168.1.101/ScaleService/";
		//public static var serverRoot:String="http://127.0.0.1/ScaleProtocol/";
		public static var serverRoot:String="http://192.168.1.104/";
		public function CommBase(url:String,needParse:Boolean=false,reTry:Boolean=false)
		{
			this.needParse=needParse;
			if(reTry)
				this.HS_list=HSFactory.newHS(url,parseHSReturn,sendRetry);
			else
				this.HS_list=HSFactory.newHS(url,parseHSReturn,showError);
		}
		public function sendHS(e:Event=null):void
		{
			trace("向业务层"+this.HS_list.url+"发送请求。");
			this.HS_list.send();
		}
		protected function parseHSReturn(e:ResultEvent):void
		{
			
			trace("从业务层"+this.HS_list.url+"成功获得信息。");
			trace(e.result);
			dispatchEvent(new HSListEvent(HSListEvent.LIST_SUCCESS));
			if(needParse)
			{
				var returnCode:int
				var parseXML:XML=new XML(e.target.lastResult);
				returnCode=Number(parseXML.actionReturn);
				switch(returnCode)
				{
					case 1:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_SUCCESS));
						Alert.show("操作成功","成功！");
						break;
					case 0:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("发生未知错误","错误0000");
						break;
					case -1:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("服务器连接故障","错误:0001");
						break;
					case -2:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("记录未找到","错误:0002");
						break;
					case -3:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("成功，但传感器不存在","错误:0003");
						break;
					case -4:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("数据类型不存在","错误:0004");
						break;
					case -5:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("数据不存在","错误:0005");
						break;
					case -6:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("长地址不存在","错误:0006");
						break;
					case -7:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("类型不存在","错误:0007");
						break;
					case -8:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("传感器不存在","错误:0008");
						break;
					case -9:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("传感器已注册","错误:0009");
						break;
					case -10:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("用户登录失败","错误:0010");
						break;
					case -11:
						dispatchEvent(new HSListEvent(HSListEvent.OPERATION_FAIL));
						Alert.show("当前操作列表中还有未执行的操作，请稍后操作","错误:0011");
						break;
					
				}
			}
		}
		protected function showError(e:FaultEvent):void
		{
			trace("业务层"+this.HS_list.url+"连接失败，放弃连接。");
			Alert.show("服务连接失败：网络连接出现故障。"+this.HS_list.url,"业务层连接故障");
		}
		protected function sendRetry(e:FaultEvent):void
		{
			trace("业务层"+this.HS_list.url+"连接失败，正在重试...");
			this.sendHS();
		}
	}
}