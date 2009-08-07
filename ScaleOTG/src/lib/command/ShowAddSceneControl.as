package lib.command
{
	import lib.interfaces.ICommand;
	
	import mx.managers.PopUpManager;
	
	import visualComponents.titleWindows.TAddSensorControl;
	public class ShowAddSceneControl extends CmdBase implements ICommand
	{
		private var pwAddSceneControl:TAddSensorControl;
		private var _sceneName:String;
		public function ShowAddSceneControl(sceneName:String)
		{
			this._sceneName=sceneName;
			super();
		}
		
		public function execute():void
		{
			pwAddSceneControl=PopUpManager.createPopUp(CmdBase.mainApp,TAddSensorControl,false) as TAddSensorControl;
			pwAddSceneControl.sceneName=_sceneName;
		}
		
	}
}