package lib.command
{
	import lib.interfaces.ICommand;
	
	import mx.managers.PopUpManager;
	
	import visualComponents.titleWindows.TAddSensor;
	public class ShowAddSensor extends CmdBase implements ICommand
	{
		private var pwAddSensor:TAddSensor;
		private var _sensorTag:String;
		public function ShowAddSensor(sensorTag:String="")
		{
			this._sensorTag=sensorTag;
			super();
		}
		
		public function execute():void
		{
			pwAddSensor=PopUpManager.createPopUp(CmdBase.mainApp,TAddSensor,false) as TAddSensor;
			if(_sensorTag!="")
			{
				pwAddSensor.sensorTag=_sensorTag;
			}
		}
		
	}
}