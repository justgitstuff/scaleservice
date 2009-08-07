package lib.communicator.operation
{
	import lib.communicator.CommBase;

	public class EditSensorDataType extends CommBase
	{
		public function EditSensorDataType()
		{
			super(serverRoot+"edit_dataType",true);
		}
		public function set typeName(value:String):void
		{
			HS_list.request.typeName=value;
		}
		public function set unit(value:String):void
		{
			HS_list.request.unit=value;
		}
		public function set maxCustom(value:Number):void
		{
			HS_list.request.maxCustom=value;
		}
		public function set minCustom(value:Number):void
		{
			HS_list.request.minCustom=value;
		}
	}
}