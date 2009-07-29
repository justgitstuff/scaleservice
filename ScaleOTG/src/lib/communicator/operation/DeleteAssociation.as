package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class DeleteAssociation extends CommBase
	{
		public var associationID:uint;
		public function DeleteAssociation()
		{
			super(serverRoot+"manual/deleteAssociation.php",true);
		}
		override public function sendHS(e:Event=null):void
		{
			HS_list.request.associationID=associationID;
			super.sendHS();
		}
	}
}