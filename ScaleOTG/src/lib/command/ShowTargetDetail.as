package lib.command
{
	import lib.communicator.download.AllCommand;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import visualComponents.detailContent.ManualControl;
	public final class ShowTargetDetail extends CmdBase implements ICommand
	{
		private var _deviceTag:String;
		
		public function ShowTargetDetail(deviceTag:String)
		{
			this._deviceTag=deviceTag;
			super();
		}

		public function execute():void
		{
			CmdBase.mainApp.currentState="sDetail";
			CmdBase.mainApp.btn_showState(0);
			var cl:ManualControl=CmdBase.mainApp.detailPanel.getChildAt(0) as ManualControl;
			cl.deviceTag=_deviceTag;
		}
	}
}