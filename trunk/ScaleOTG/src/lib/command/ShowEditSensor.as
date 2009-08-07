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
		private var _sensorTag:String;
		private var pwEditSensor:TEditSensor;
		private var singleSensor:Single_Sensor;
		public function ShowEditSensor(sensorTag:String)
		{
			this._sensorTag=sensorTag;
			super();
		}
		
		public function execute():void
		{
			pwEditSensor=PopUpManager.createPopUp(CmdBase.mainApp,TEditSensor,false) as TEditSensor;
			singleSensor=new Single_Sensor(_sensorTag);
			singleSensor.addEventListener(HSListEvent.LIST_SUCCESS,onReceiveSensorData);
			singleSensor.sendHS();
		}
		private function onReceiveSensorData(e:Event=null):void
		{
			pwEditSensor.sensorTag=singleSensor.recordXML.row.sensorTag[0];
			pwEditSensor.ipt_sensorName.text=singleSensor.recordXML.row.sensorName[0];
			pwEditSensor.ipt_location.text=singleSensor.recordXML.row.location[0]
			pwEditSensor.ipt_manufacture.text=singleSensor.recordXML.row.manufacturer[0];
			pwEditSensor.ipt_description.text=singleSensor.recordXML.row.description[0];
			pwEditSensor.ipt_memo.text=singleSensor.recordXML.row.memo[0];
			pwEditSensor.ipt_typeName.text=singleSensor.recordXML.row.typeName[0];
		}
		
	}
}