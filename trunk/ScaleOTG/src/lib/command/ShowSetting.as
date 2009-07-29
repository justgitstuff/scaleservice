package lib.command
{
	import lib.interfaces.ICommand;
	import mx.managers.PopUpManager;
	import visualComponents.titleWindows.TSetting;

	public class ShowSetting extends CmdBase implements ICommand
	{
		private static var pwSetting:TSetting;
		public function ShowSetting()
		{
			super();
		}
		
		public function execute():void
		{
			if(pwSetting==null)
				pwSetting=new TSetting();
			PopUpManager.addPopUp(pwSetting,CmdBase.mainApp,true);
		}
		
	}
}