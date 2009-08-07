package lib.command
{
	import lib.interfaces.ICommand;
	
	import visualComponents.detailContent.ControlEdit;

	public class ShowOperationDetail extends CmdBase implements ICommand
	{
		private var _typeName:String;
		private var _deviceTag:String;
		private var _intro:String;
		private var _command:String;
		private var _parameter:String;
		public function ShowOperationDetail(typeName:String,deviceTag:String,intro:String,command:String,parameter:String)
		{
			this._typeName=typeName;
			this._deviceTag=deviceTag;
			this._intro=intro;
			this._command=command;
			this._parameter=parameter;
			super();
		}
		
		public function execute():void
		{
			CmdBase.mainApp.currentState="sDetail";
			CmdBase.mainApp.btn_showState(0);
			var cl:ControlEdit=CmdBase.mainApp.detailPanel.getChildAt(0) as ControlEdit;
			cl.intro=_intro;
			cl.typeName=_typeName;
			cl.deviceTag=_deviceTag;
			cl.command=_command;
			cl.parameter=_parameter;
		}
	}
}