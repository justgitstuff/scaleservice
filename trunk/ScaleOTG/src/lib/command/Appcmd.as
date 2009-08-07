package lib.command
{
	import lib.interfaces.ICommand;
	
	public class Appcmd
	{
		private static var unique:Appcmd;
		public var showSensorDataType:ICommand;
		public var showOperations:ICommand;
		public var showScene:ICommand;
		public var showTarget:ICommand;
		public var showDetailData:ICommand;
		public var showSensorManager:ICommand;
		public var showEditSensor:ICommand;
		public var showEditSensorDataType:ICommand;
		public var showEditTarget:ICommand;
		public var showAddSensor:ICommand;
		public var showAddScene:ICommand;
		public var showAddSceneControl:ICommand;
		public var showAddOperation:ICommand;
		public var showSetting:ICommand;
		public var showRSS:ICommand;
		public var showAssociation:ICommand;
		public function Appcmd()
		{
			showSensorDataType=new ShowSensorDataType();
			showOperations=new ShowOperations();
			showScene=new ShowScene();
			showTarget=new ShowTarget();
			showDetailData=new ShowSensorData(null);
			showSensorManager=new ShowSensorManager();
			showEditSensor=new ShowEditSensor(null);
			showEditSensorDataType=new ShowEditSensorDataType(null);
			showAddScene=new ShowAddScene();
			showAddSceneControl=new ShowAddSceneControl(null);
			showAddOperation=new ShowAddOperation();
			showSetting=new ShowSetting();
		}
		public static function getInstance():Appcmd
		{
			if(unique==null)
				unique=new Appcmd;
			return unique;
		}
	}
}