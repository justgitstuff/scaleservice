package lib.communicator.operation
{
	import flash.events.Event;
	
	import lib.communicator.CommBase;

	public class AddSceneControl extends CommBase
	{
		public function AddSceneControl()
		{
			super(serverRoot+"add_sceneControl",true);
		}
		public function set sceneName(value:String):void
		{
			HS_list.request.sceneName=value;
		}
		public function set deviceTag(value:String):void
		{
			HS_list.request.deviceTag=value;
		}
		public function set command(value:String):void
		{
			HS_list.request.command=value;
		}
		public function set parameter(value:String):void
		{
			HS_list.request.parameter=value;
		}
	}
}