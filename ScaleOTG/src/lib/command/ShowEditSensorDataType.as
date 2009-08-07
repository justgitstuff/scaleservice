package lib.command
{
	import flash.events.Event;
	
	import lib.communicator.download.Single_SensorDataType;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import mx.managers.PopUpManager;
	
	import visualComponents.titleWindows.TEditDataType;

	public class ShowEditSensorDataType extends CmdBase implements ICommand
	{
		private var _typeName:String;
		private var pwEditDataType:TEditDataType;
		private var singleSensorDataType:Single_SensorDataType;
		public function ShowEditSensorDataType(typeName:String)
		{
			this._typeName=typeName;
			super();
		}
		
		public function execute():void
		{
			pwEditDataType=PopUpManager.createPopUp(CmdBase.mainApp,TEditDataType,false) as TEditDataType;
			singleSensorDataType=new Single_SensorDataType(_typeName);
			singleSensorDataType.addEventListener(HSListEvent.LIST_SUCCESS,onReceiveData);
			singleSensorDataType.sendHS();
		}
		private function onReceiveData(e:Event=null):void
		{
			pwEditDataType.typeName=singleSensorDataType.recordXML.row.typeName[0];
			pwEditDataType.ipt_unit.text=singleSensorDataType.recordXML.row.unit[0];
			pwEditDataType.u1.text=singleSensorDataType.recordXML.row.unit[0];
			pwEditDataType.u2.text=singleSensorDataType.recordXML.row.unit[0];
			pwEditDataType.ipt_maxCustom.value=Number(singleSensorDataType.recordXML.row.maxCustom);
			pwEditDataType.ipt_minCustom.value=Number(singleSensorDataType.recordXML.row.minCustom);
		}
		
	}
}