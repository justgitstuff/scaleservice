package lib.command
{
	import lib.interfaces.ICommand;
	
	import mx.managers.PopUpManager;
	
	import visualComponents.titleWindows.TAddOperation;
	public class ShowAddOperation extends CmdBase implements ICommand
	{
		private var pwAddOperation:TAddOperation;
		private var _defaultTypeName:String;
		
		public function ShowAddOperation(defaultTypeName:String=null)
		{
			this._defaultTypeName=defaultTypeName;
			super();
		}
		
		public function execute():void
		{
			pwAddOperation=PopUpManager.createPopUp(CmdBase.mainApp,TAddOperation,false) as TAddOperation;
			//添加默认DataType
		}
		
	}
}