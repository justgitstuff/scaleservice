package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class AddAssociation extends CommBase
	{
		public var lAddrID_1:String;
		public var commandID_1:String;
		public var lAddrID_2:String;
		public var commandID_2:String;
		public function AddAssociation()
		{
			super(serverRoot+"manual/addAssociation.php",true);
		}
		
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.lAddrID_1=lAddrID_1;
			HS_list.request.commandID_1=commandID_1;
			HS_list.request.lAddrID_2=lAddrID_2;
			HS_list.request.commandID_2=commandID_2;
			super.sendHS();
		}
	}
}