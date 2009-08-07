package lib.command
{
	import lib.communicator.download.AllSensorData;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import visualComponents.detailContent.SensorData;
	public final class ShowSensorData extends CmdBase implements ICommand
	{
		private var _typeName:String;
		private var allSensorData:AllSensorData;
		public function ShowSensorData(typeName:String)
		{
			this._typeName=typeName;
			super();
		}

		public function execute():void
		{
			CmdBase.mainApp.currentState="sDetail";
			CmdBase.mainApp.btn_showState(0);
			allSensorData=AllSensorData.getInstance();
			allSensorData.typeName=_typeName;
			allSensorData.addEventListener(HSListEvent.LIST_SUCCESS,onDataLoaded);
			allSensorData.sendHS();
			
		}
		private function onDataLoaded(e:HSListEvent):void
		{
			var sensorData:SensorData=CmdBase.mainApp.detailPanel.getChildAt(0) as SensorData;
			sensorData.typeName=_typeName;
			sensorData.btn_operation.enabled=true;
			sensorData.btn_dataType.enabled=true;
			sensorData.btn_delete.enabled=true;
			sensorData.sensorDataGrid.dataProvider=allSensorData.recordList;
			sensorData.sensorDataChart.dataProvider=allSensorData.recordList;
			sensorData.sensorDataChartH.dataProvider=allSensorData.recordList;
			
		}
	}
}