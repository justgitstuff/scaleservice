package lib.command
{
	import flash.events.Event;
	
	import lib.communicator.download.Single_Target;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import mx.managers.PopUpManager;
	
	import visualComponents.titleWindows.TEditTarget;

	public class ShowEditTarget extends CmdBase implements ICommand
	{
		private var _deviceTag:String;
		private var pwEditTarget:TEditTarget;
		private var single_target:Single_Target;
		public function ShowEditTarget(deviceTag:String)
		{
			this._deviceTag=deviceTag;
			super();
		}
		
		public function execute():void
		{
			pwEditTarget=PopUpManager.createPopUp(CmdBase.mainApp,TEditTarget,false) as TEditTarget;
			single_target=new Single_Target();
			single_target.deviceTag=_deviceTag;
			single_target.addEventListener(HSListEvent.LIST_SUCCESS,onReceiveData);
			single_target.sendHS();
		}
		private function onReceiveData(e:Event=null):void
		{
			pwEditTarget.deviceTag=single_target.recordXML.row.deviceTag[0];
			pwEditTarget.ipt_intro.text=single_target.recordXML.row.intro[0];
		}
		
	}
}