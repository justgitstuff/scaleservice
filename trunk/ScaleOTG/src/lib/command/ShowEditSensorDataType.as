package lib.command
{
	import flash.events.Event;
	
	import lib.communicator.download.Single_SensorDataType;
	import lib.interfaces.ICommand;
	import mx.managers.PopUpManager;
	import lib.events.HSListEvent;
	import visualComponents.titleWindows.TEditDataType;

	public class ShowEditSensorDataType extends CmdBase implements ICommand
	{
		private var dataTypeID:uint;
		private var pwEditDataType:TEditDataType;
		private var singleSensorDataType:Single_SensorDataType;
		public function ShowEditSensorDataType(dataTypeID:uint)
		{
			this.dataTypeID=dataTypeID;
			super();
		}
		
		public function execute():void
		{
			pwEditDataType=PopUpManager.createPopUp(CmdBase.mainApp,TEditDataType,false) as TEditDataType;
			singleSensorDataType=new Single_SensorDataType(dataTypeID);
			singleSensorDataType.addEventListener(HSListEvent.LIST_SUCCESS,onReceiveData);
			singleSensorDataType.sendHS();
		}
		private function onReceiveData(e:Event=null):void
		{
			pwEditDataType.sensorDataTypeID=Number(singleSensorDataType.recordXML.row.@sensorDataTypeID);
			pwEditDataType.ipt_dataTypeName.text=singleSensorDataType.recordXML.row.@typeName;
			pwEditDataType.ipt_unit.text=singleSensorDataType.recordXML.row.@unit;
			pwEditDataType.u1.text=singleSensorDataType.recordXML.row.@unit;
			pwEditDataType.u2.text=singleSensorDataType.recordXML.row.@unit;
			pwEditDataType.ipt_maxCustom.value=Number(singleSensorDataType.recordXML.row.@maxCustom);
			pwEditDataType.ipt_minCustom.value=Number(singleSensorDataType.recordXML.row.@minCustom);
		}
		
	}
}