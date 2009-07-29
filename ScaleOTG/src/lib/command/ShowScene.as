package lib.command
{
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import lib.communicator.download.AllScene;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import mx.controls.Alert;
	import mx.controls.List;
	import mx.core.ClassFactory;
	import mx.events.CloseEvent;
	import mx.events.ListEvent;
	
	import visualComponents.detailContent.SceneDetail;
	import visualComponents.itemRenderers.*;
	public final class ShowScene extends CmdBase implements ICommand
	{
		private var allScene:AllScene;
		private var timer:Timer=new Timer(1000,1);
		public function ShowScene()
		{
			super();
			timer.addEventListener(TimerEvent.TIMER,visualComplete);
		}

		public function execute():void
		{
			CmdBase.mainApp.currentState="sIndex";
			CmdBase.mainApp.btn_showState(2);
			if(CmdBase.mainApp.searchFunction!=null)
			{
				CmdBase.mainApp.ipt_search.text="";
				CmdBase.mainApp.searchFunction();
			}
			allScene=AllScene.getInstance();
			allScene.addEventListener(HSListEvent.LIST_SUCCESS,onDataLoaded);
			timer.start();
		}
		private function visualComplete(e:TimerEvent):void
		{
			allScene.sendHS();
		}
		private function onDataLoaded(e:HSListEvent):void
		{
			if(allScene.recordList.length)
			{
				var sd:SceneDetail=new SceneDetail();
				var ilst:List;
				
				CmdBase.mainApp.indexPanel.title="所有场景";
				CmdBase.mainApp.detailPanel.title="场景操作";
				
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
				ilst.itemRenderer=new ClassFactory(SceneItem);
				ilst.dataProvider=allScene.recordList;
				CmdBase.mainApp.VSI.removeAllChildren();
				CmdBase.mainApp.VSI.addChild(ilst);
				
				CmdBase.mainApp.currentVSIState=2;
				CmdBase.mainApp.searchFunction=allScene.recordList.refresh;
				allScene.recordList.filterFunction=sceneFileter;
				allScene.recordList.refresh();
			}
			else
				Alert.show("当前尚未建立任何场景。场景是一系列操作的组合，可以快速的变换家庭的环境设定。是否现在建立场景？","建立场景",Alert.YES|Alert.NO,CmdBase.mainApp,emptyHandler);
		}
		private function emptyHandler(e:CloseEvent):void
		{
			if(e.detail==Alert.YES)
			{
				var invoker:Appcmd=Appcmd.getInstance();
				invoker.showAddScene.execute();
			}	
		}
		public function sceneFileter(item:Object):Boolean
		{
			var cString:String=CmdBase.mainApp.ipt_search.text;
			if((cString==""
			 || String(item.@sceneName).indexOf(cString)!=-1)
			 || String(item.@keyWord).indexOf(cString)!=-1)
				return true;
			else
				return false; 
		} 
	}
}