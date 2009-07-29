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
			showDetailData=new ShowSensorData(0);
			showSensorManager=new ShowSensorManager();
			showEditSensor=new ShowEditSensor(0);
			showEditSensorDataType=new ShowEditSensorDataType(0);
			showAddScene=new ShowAddScene();
			showAddSceneControl=new ShowAddSceneControl(0);
			showAddOperation=new ShowAddOperation();
			showSetting=new ShowSetting();
			showRSS=new ShowRSS();
			showAssociation=new ShowAssociation();
		}
		public static function getInstance():Appcmd
		{
			if(unique==null)
				unique=new Appcmd;
			return unique;
		}
	}
}