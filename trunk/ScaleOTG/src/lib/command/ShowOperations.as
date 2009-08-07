package lib.command
{
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import lib.communicator.download.AllOperations;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import mx.controls.Alert;
	import mx.controls.List;
	import mx.core.ClassFactory;
	import mx.events.CloseEvent;
	import mx.events.ListEvent;
	
	import visualComponents.detailContent.ControlEdit;
	import visualComponents.itemRenderers.*;
	public final class ShowOperations extends CmdBase implements ICommand
	{
		private var allOperations:AllOperations;
		private var timer:Timer=new Timer(1000,1);
		public function ShowOperations()
		{
			super();
			timer.addEventListener(TimerEvent.TIMER,visualComplete);
		}

		public function execute():void
		{
			CmdBase.mainApp.currentState="sIndex";
			CmdBase.mainApp.btn_showState(3);
			if(CmdBase.mainApp.searchFunction!=null)
			{
				CmdBase.mainApp.ipt_search.text="";
				CmdBase.mainApp.searchFunction();
			}
			allOperations=AllOperations.getInstance();
			allOperations.addEventListener(HSListEvent.LIST_SUCCESS,onDataLoaded);
			timer.start();
		}
		private function visualComplete(e:TimerEvent):void
		{
			allOperations.sendHS();
		}
		private function onDataLoaded(e:HSListEvent):void
		{
			if(allOperations.recordList.length)
			{
				CmdBase.mainApp.emptyImage.visible=false;
				var sd:ControlEdit=new ControlEdit();
				var ilst:List;
				
				CmdBase.mainApp.indexPanel.title="所有自动操作";
				CmdBase.mainApp.detailPanel.title="操作设定";
				
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
				ilst.itemRenderer=new ClassFactory(AutoCtrlItem);
				ilst.dataProvider=allOperations.recordList;
				CmdBase.mainApp.VSI.removeAllChildren();
				CmdBase.mainApp.VSI.addChild(ilst);
				
				CmdBase.mainApp.currentVSIState=3;
				CmdBase.mainApp.searchFunction=allOperations.recordList.refresh;
				allOperations.recordList.filterFunction=operationFileter;
				allOperations.recordList.refresh();
			}
			else
			{
				CmdBase.mainApp.currentState="sIndex";
				CmdBase.mainApp.VSI.removeAllChildren();
				CmdBase.mainApp.emptyImage.visible=true;
				Alert.show("您当前还没有创建任何自动操作，是否创建一个操作？","创建自动操作",Alert.YES|Alert.NO,CmdBase.mainApp,emptyHandler);
			}
		}
		private function emptyHandler(e:CloseEvent):void
		{
			if(e.detail==Alert.YES)
			{
				var invoker:Appcmd=Appcmd.getInstance();
				invoker.showAddOperation.execute();
			}	
		}
		public function operationFileter(item:Object):Boolean
		{
			var cString:String=CmdBase.mainApp.ipt_search.text;
			if(
				cString==""
			 || String(item.intro).indexOf(cString)!=-1
			 || String(item.control_action).indexOf(cString)!=-1
			 || String(item.typeName).indexOf(cString)!=-1
			 )
				return true;
			else
				return false; 
		} 
	}
}