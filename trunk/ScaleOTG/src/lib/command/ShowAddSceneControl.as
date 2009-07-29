package lib.command
{
	import lib.interfaces.ICommand;
	
	import mx.managers.PopUpManager;
	
	import visualComponents.titleWindows.TAddSensorControl;
	public class ShowAddSceneControl extends CmdBase implements ICommand
	{
		private var pwAddSceneControl:TAddSensorControl;
		private var sceneID:uint;
		public function ShowAddSceneControl(sceneID:uint)
		{
			this.sceneID=sceneID;
			super();
		}
		
		public function execute():void
		{
			pwAddSceneControl=PopUpManager.createPopUp(CmdBase.mainApp,TAddSensorControl,false) as TAddSensorControl;
			pwAddSceneControl.sceneID=this.sceneID;
		}
		
	}
}