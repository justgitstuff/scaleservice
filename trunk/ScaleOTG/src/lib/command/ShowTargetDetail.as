package lib.command
{
	import lib.communicator.download.AllCommand;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import visualComponents.detailContent.ManualControl;
	import visualComponents.detailContent.SensorData;
	public final class ShowTargetDetail extends CmdBase implements ICommand
	{
		public var cID:String;
		private var allCommand:AllCommand
		public function ShowTargetDetail(targetID:String)
		{
			this.cID=targetID;
			super();
		}

		public function execute():void
		{
			CmdBase.mainApp.currentState="sDetail";
			CmdBase.mainApp.btn_showState(0);
			allCommand=AllCommand.getInstance();
			allCommand.addEventListener(HSListEvent.LIST_SUCCESS,onCMDLoaded);
			allCommand.sendHS();
			
		}
		private function onCMDLoaded(e:HSListEvent):void
		{
			var cl:ManualControl=CmdBase.mainApp.detailPanel.getChildAt(0) as ManualControl;
			cl.controlTargetID=cID;
			cl.cmdList.dataProvider=allCommand.recordList;
		}
	}
}