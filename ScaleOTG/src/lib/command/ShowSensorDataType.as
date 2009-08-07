package lib.command
{
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import lib.communicator.download.FilledDataType;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import mx.controls.Alert;
	import mx.controls.List;
	import mx.core.ClassFactory;
	import mx.events.CloseEvent;
	import mx.events.ListEvent;
	
	import visualComponents.detailContent.SensorData;
	import visualComponents.itemRenderers.*;
	public final class ShowSensorDataType extends CmdBase implements ICommand
	{
		private var filledDataType:FilledDataType;
		private var timer:Timer=new Timer(1000,1);
		public function ShowSensorDataType()
		{
			super();
			timer.addEventListener(TimerEvent.TIMER,visualComplete);
		}

		public function execute():void
		{
			CmdBase.mainApp.currentState="sIndex";
			CmdBase.mainApp.btn_showState(1);
			if(CmdBase.mainApp.searchFunction!=null)
			{
				CmdBase.mainApp.ipt_search.text="";
				CmdBase.mainApp.searchFunction();
			}
			filledDataType=FilledDataType.getInstance();
			filledDataType.addEventListener(HSListEvent.LIST_SUCCESS,onDataTypeLoaded);
			timer.start();
		}
		private function visualComplete(e:TimerEvent):void
		{
			filledDataType.sendHS();
		}
		private function onDataTypeLoaded(e:HSListEvent):void
		{
			if(filledDataType.recordList.length)
			{
				CmdBase.mainApp.emptyImage.visible=false;
				var sd:SensorData=new SensorData();
				var ilst:List;
				
				CmdBase.mainApp.indexPanel.title="所有数据类型";
				CmdBase.mainApp.detailPanel.title="数据详情";
				
				sd.percentHeight=100;
				sd.percentWidth=100;
				CmdBase.mainApp.detailPanel.removeAllChildren();
				CmdBase.mainApp.detailPanel.addChild(sd);
			
				ilst=new List();
				ilst.x=0;
				ilst.y=23;
				ilst.percentHeight=100;
				ilst.percentWidth=100;
				ilst.addEventListener(ListEvent.CHANGE,CmdBase.mainApp.onVSIChange);
				ilst.itemRenderer=new ClassFactory(SensorDataTypeItem);
				ilst.dataProvider=filledDataType.recordList;
				CmdBase.mainApp.VSI.removeAllChildren();
				CmdBase.mainApp.VSI.addChild(ilst);
				
				CmdBase.mainApp.currentVSIState=1;
				filledDataType.recordList.filterFunction=dataTypeFileter;
				CmdBase.mainApp.searchFunction=filledDataType.recordList.refresh;
				filledDataType.recordList.refresh();
			}
			else
			{
				CmdBase.mainApp.currentState="sIndex";
				CmdBase.mainApp.VSI.removeAllChildren();
				CmdBase.mainApp.emptyImage.visible=true;
				Alert.show("当前还未有任何数据类型，请确认您已将传感器注册入网。是否现在注册传感器？","未找到正在工作的传感器",Alert.YES|Alert.NO,CmdBase.mainApp,emptyHandler);
			}
		}
		private function emptyHandler(e:CloseEvent):void
		{
			if(e.detail==Alert.YES)
			{
				var invoker:Appcmd=Appcmd.getInstance();
				invoker.showSensorManager.execute();
			}
		}
		public function dataTypeFileter(item:Object):Boolean
		{
			var cString:String=CmdBase.mainApp.ipt_search.text;
			if(
				cString==""
			 || String(item.unit).indexOf(cString)!=-1
			 || String(item.typeName).indexOf(cString)!=-1
			 )
				return true;
			else
				return false; 
		} 
	}
}