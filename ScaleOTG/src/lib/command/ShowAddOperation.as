package lib.command
{
	import lib.interfaces.ICommand;
	
	import mx.managers.PopUpManager;
	
	import visualComponents.titleWindows.TAddOperation;
	public class ShowAddOperation extends CmdBase implements ICommand
	{
		private var pwAddOperation:TAddOperation;
		private var dataTypeOrder:uint;
		public function ShowAddOperation(dataTypeOrder:uint=0)
		{
			this.dataTypeOrder=dataTypeOrder;
			super();
		}
		
		public function execute():void
		{
			pwAddOperation=PopUpManager.createPopUp(CmdBase.mainApp,TAddOperation,false) as TAddOperation;
			if(dataTypeOrder)
				pwAddOperation.dOrder=dataTypeOrder;
		}
		
	}
}