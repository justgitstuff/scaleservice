package lib.command
{
	import flash.events.Event;
	
	import lib.communicator.download.Single_Operation;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import visualComponents.detailContent.ControlEdit;

	public class ShowOperationDetail extends CmdBase implements ICommand
	{
		public var lID:String;
		public var cID:String;
		public var sID:uint;
		private var sop:Single_Operation;
		public function ShowOperationDetail(lAddrID:String,commandID:String,sensorDataTypeID:uint)
		{
			this.lID=lAddrID;
			this.cID=commandID;
			this.sID=sensorDataTypeID;
			super();
		}
		
		public function execute():void
		{
			CmdBase.mainApp.currentState="sDetail";
			CmdBase.mainApp.btn_showState(0);
			sop=new Single_Operation();
			sop.lAddrID=lID;
			sop.commandID=cID;
			sop.sensorDataTypeID=sID;
			sop.addEventListener(HSListEvent.LIST_SUCCESS,onDataLoaded);
			sop.sendHS();
		}
		private function onDataLoaded(e:Event=null):void
		{
			var controlEdit:ControlEdit=CmdBase.mainApp.detailPanel.getChildAt(0) as ControlEdit;
			controlEdit.dID=Number(sop.recordXML.row.@sensorDataTypeID);
			controlEdit.cID=sop.recordXML.row.@commandID;
			controlEdit.tID=sop.recordXML.row.@lAddrID;
			controlEdit.btn_delete.enabled=true;
			controlEdit.btn_save.enabled=true;
			controlEdit.ipt_below.enabled=true;
			controlEdit.ipt_over.enabled=true;
			controlEdit.refreshSetting();
			if(Number(sop.recordXML.row.@inc))//以提高
				controlEdit.ipt_below.selected=true;
			else
				controlEdit.ipt_over.selected=true;
			
		}
	}
}