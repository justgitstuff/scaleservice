package lib.communicator.operation
{
	import lib.communicator.CommBase;

	public class DeleteDataType extends CommBase
	{
		public function DeleteDataType()
		{
			super(serverRoot+"delete_dataType",true);
		}
		public function set typeName(value:String):void
		{
			HS_list.request.typeName=value;
		}
	}
}