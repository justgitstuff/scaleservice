package lib.communicator.download
{
	import lib.communicator.DownloadBase;
	public class Single_SensorDataType extends DownloadBase
	{
		public function Single_SensorDataType(typeName:String)
		{
			//super(serverRoot+"SensorDataType_Single.xml");
			super(serverRoot+"view_dataType");
			HS_list.request.typeName=typeName;
		}

	}
}