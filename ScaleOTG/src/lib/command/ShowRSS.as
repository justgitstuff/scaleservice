package lib.command
{
	import lib.interfaces.ICommand;
	
	import mx.managers.PopUpManager;
	
	import visualComponents.titleWindows.TRSS;
	public class ShowRSS extends CmdBase implements ICommand
	{
		private var pwRSS:TRSS;
		private var dataTypeOrder:uint;
		private var _topic:String;
		public function ShowRSS(topic:String="")
		{
			this._topic=topic;
			super();
		}
		
		public function execute():void
		{
			pwRSS=PopUpManager.createPopUp(CmdBase.mainApp,TRSS,false) as TRSS;
			if(_topic!="")
			{
				pwRSS.searchInfo(_topic);
			}
		}
		
	}
}