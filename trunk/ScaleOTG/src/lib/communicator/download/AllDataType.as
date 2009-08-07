package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class AllDataType extends DownloadBase
	{
		private static var unique:AllDataType;
		public function AllDataType()
		{
			super(serverRoot+"view_dataType");
			//super(serverRoot+"sensorDataType.xml");
		}
		public static function getInstance():AllDataType
		{
			if(unique==null)
				unique=new AllDataType();
			return unique;
		}
		
	}
}