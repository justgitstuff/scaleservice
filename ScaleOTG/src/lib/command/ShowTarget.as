package lib.command
{
	import lib.communicator.download.AllTarget;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import mx.controls.List;
	import mx.core.ClassFactory;
	import mx.events.ListEvent;
	import mx.controls.Alert;
	import visualComponents.detailContent.ManualControl;
	import visualComponents.detailContent.SensorData;
	import visualComponents.itemRenderers.*;
	public final class ShowTarget extends CmdBase implements ICommand
	{
		private var allTarget:AllTarget;
		private var timer:Timer=new Timer(1000,1);
		public function ShowTarget()
		{
			super();
			timer.addEventListener(TimerEvent.TIMER,visualComplete);
		}

		public function execute():void
		{
			CmdBase.mainApp.currentState="sIndex";
			CmdBase.mainApp.btn_showState(4);
			if(CmdBase.mainApp.searchFunction!=null)
			{
				CmdBase.mainApp.ipt_search.text="";
				CmdBase.mainApp.searchFunction();
			}
			allTarget=AllTarget.getInstance();
			allTarget.addEventListener(HSListEvent.LIST_SUCCESS,onTargetLoaded);
			timer.start();
		}
		private function visualComplete(e:TimerEvent):void
		{
			allTarget.sendHS();
		}
		private function onTargetLoaded(e:HSListEvent):void
		{
			if(allTarget.recordList.length)
			{
				CmdBase.mainApp.emptyImage.visible=false;
				var sd:ManualControl=new ManualControl();
				var ilst:List;
				CmdBase.mainApp.indexPanel.title="所有设备";
				CmdBase.mainApp.detailPanel.title="操作";
				
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
				ilst.itemRenderer=new ClassFactory(TargetItem);
				ilst.dataProvider=allTarget.recordList;
				CmdBase.mainApp.VSI.removeAllChildren();
				CmdBase.mainApp.VSI.addChild(ilst);
				
				CmdBase.mainApp.currentVSIState=4;
				allTarget.recordList.filterFunction=dataTypeFileter;
				CmdBase.mainApp.searchFunction=allTarget.recordList.refresh;
				allTarget.recordList.refresh();
			}
			else
			{
				CmdBase.mainApp.currentState="sIndex";
				CmdBase.mainApp.VSI.removeAllChildren();
				CmdBase.mainApp.emptyImage.visible=true;
				Alert.show("未找到可以控制的设备。请确认您已购买Scale兼容的被控设备，且已打开其电源。","未找到可控设备");
			}
		}
		public function dataTypeFileter(item:Object):Boolean
		{
			var cString:String=CmdBase.mainApp.ipt_search.text;
			if(
				cString==""
			 || String(item.deviceTag).indexOf(cString)!=-1
			 || String(item.intro).indexOf(cString)!=-1
			 || String(item.currentState).indexOf(cString)!=-1
			 )
				return true;
			else
				return false; 
		} 
	}
}