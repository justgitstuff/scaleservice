package lib.command
{
	import lib.communicator.download.AllSceneControl;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import visualComponents.detailContent.SceneDetail;
	public final class ShowSceneControl extends CmdBase implements ICommand
	{
		private var _sceneControl:String;
		private var _sceneName:String;
		private var allSceneControl:AllSceneControl;
		public function ShowSceneControl(sceneName:String)
		{
			this._sceneName=sceneName;
			super();
		}

		public function execute():void
		{
			CmdBase.mainApp.currentState="sDetail";
			CmdBase.mainApp.btn_showState(0);
			allSceneControl=AllSceneControl.getInstance();
			allSceneControl.sceneName=_sceneName;
			allSceneControl.addEventListener(HSListEvent.LIST_SUCCESS,onDataLoaded);
			allSceneControl.sendHS();
			
		}
		private function onDataLoaded(e:HSListEvent):void
		{
			var sceneDetail:SceneDetail=CmdBase.mainApp.detailPanel.getChildAt(0) as SceneDetail;
			sceneDetail.sceneName=_sceneName;
			sceneDetail.btn_addControl.enabled=true;
			sceneDetail.btn_delete.enabled=true;
			sceneDetail.btn_enter.enabled=true;
			var invoker:Appcmd=Appcmd.getInstance();
			invoker.showAddSceneControl=new ShowAddSceneControl(_sceneName);
			sceneDetail.controlList.dataProvider=allSceneControl.recordList;
		}
	}
}