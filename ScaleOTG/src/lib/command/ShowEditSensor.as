package lib.command
{
	import flash.events.Event;
	
	import lib.communicator.download.Single_Sensor;
	import lib.events.HSListEvent;
	import lib.interfaces.ICommand;
	
	import mx.managers.PopUpManager;
	
	import visualComponents.titleWindows.TEditSensor;

	public class ShowEditSensor extends CmdBase implements ICommand
	{
		private var sensorID:uint;
		private var pwEditSensor:TEditSensor;
		private var singleSensor:Single_Sensor;
		public function ShowEditSensor(sensorID:uint)
		{
			this.sensorID=sensorID;
			super();
		}
		
		public function execute():void
		{
			pwEditSensor=PopUpManager.createPopUp(CmdBase.mainApp,TEditSensor,false) as TEditSensor;
			singleSensor=new Single_Sensor(sensorID);
			singleSensor.addEventListener(HSListEvent.LIST_SUCCESS,onReceiveSensorData);
			singleSensor.sendHS();
		}
		private function onReceiveSensorData(e:Event=null):void
		{
			pwEditSensor.sensorID=Number(singleSensor.recordXML.row.@sensorID[0]);
			pwEditSensor.ipt_sensorName.text=singleSensor.recordXML.row.@sensorName[0];
			pwEditSensor.ipt_location.text=singleSensor.recordXML.row.@location[0];
			pwEditSensor.ipt_manufacture.text=singleSensor.recordXML.row.@manufacture[0];
			pwEditSensor.ipt_description.text=singleSensor.recordXML.row.@description[0];
			pwEditSensor.ipt_memo.text=singleSensor.recordXML.row.@memo[0];
		}
		
	}
}