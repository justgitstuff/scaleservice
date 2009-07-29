package lib.command
{
	import lib.interfaces.ICommand;
	
	import mx.managers.PopUpManager;
	
	import visualComponents.titleWindows.TAddScene;
	public class ShowAddScene extends CmdBase implements ICommand
	{
		private var pwAddScene:TAddScene;
		private var dataTypeOrder:uint;
		private var _keyWord:String;
		public function ShowAddScene(keyWord:String="")
		{
			this._keyWord=keyWord;
			super();
		}
		
		public function execute():void
		{
			pwAddScene=PopUpManager.createPopUp(CmdBase.mainApp,TAddScene,false) as TAddScene;
			if(_keyWord!="")
			{
				pwAddScene.ipt_keyWord.text=_keyWord;
				pwAddScene.ipt_keyWord.enabled=false;
			}
		}
		
	}
}