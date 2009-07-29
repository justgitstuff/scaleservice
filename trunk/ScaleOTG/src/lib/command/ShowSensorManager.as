package lib.command
{
	import lib.interfaces.ICommand;
	import mx.managers.PopUpManager;
	import visualComponents.titleWindows.TSensorManager;

	public final class ShowSensorManager extends CmdBase implements ICommand
	{
		private static var pwSensorManager:TSensorManager;
		public function ShowSensorManager()
		{
			super();
		}
		
		public function execute():void
		{
			if(pwSensorManager==null)
				pwSensorManager=new TSensorManager();
			PopUpManager.addPopUp(pwSensorManager,CmdBase.mainApp,true);
		}
		
	}
}