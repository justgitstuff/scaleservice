package lib.command
{
	import lib.communicator.download.Single_Sensor;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import mx.managers.PopUpManager;
	
	import visualComponents.titleWindows.TAssociation;
	import visualComponents.titleWindows.TEditSensor;

	public class ShowAssociation extends CmdBase implements ICommand
	{
		private var pwEditSensor:TAssociation;
		public function ShowAssociation()
		{
			super();
		}
		
		public function execute():void
		{
			pwEditSensor=PopUpManager.createPopUp(CmdBase.mainApp,TAssociation,false) as TAssociation;
		}
	}
}