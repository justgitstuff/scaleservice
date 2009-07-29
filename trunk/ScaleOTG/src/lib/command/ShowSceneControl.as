package lib.command
{
	import lib.communicator.download.AllSceneControl;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import visualComponents.detailContent.SceneDetail;
	import visualComponents.detailContent.SensorData;
	public final class ShowSceneControl extends CmdBase implements ICommand
	{
		private var sceneID:uint;
		private var allSceneControl:AllSceneControl;
		public function ShowSceneControl(sceneID:uint)
		{
			this.sceneID=sceneID;
			super();
		}

		public function execute():void
		{
			CmdBase.mainApp.currentState="sDetail";
			CmdBase.mainApp.btn_showState(0);
			allSceneControl=AllSceneControl.getInstance();
			allSceneControl.sceneID=sceneID;
			allSceneControl.addEventListener(HSListEvent.LIST_SUCCESS,onDataLoaded);
			allSceneControl.sendHS();
			
		}
		private function onDataLoaded(e:HSListEvent):void
		{
			var sceneDetail:SceneDetail=CmdBase.mainApp.detailPanel.getChildAt(0) as SceneDetail;
			sceneDetail.sceneID=sceneID;
			sceneDetail.btn_addControl.enabled=true;
			sceneDetail.btn_delete.enabled=true;
			sceneDetail.btn_save.enabled=true;
			sceneDetail.btn_enter.enabled=true;
			var invoker:Appcmd=Appcmd.getInstance();
			invoker.showAddSceneControl=new ShowAddSceneControl(sceneID);
			sceneDetail.controlList.dataProvider=allSceneControl.recordList;
		}
	}
}