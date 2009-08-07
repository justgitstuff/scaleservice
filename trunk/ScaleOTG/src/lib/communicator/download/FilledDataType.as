package lib.communicator.download
{
	import lib.communicator.DownloadBase;

	public class FilledDataType extends DownloadBase
	{
		private static var unique:FilledDataType;
		public function FilledDataType()
		{
			super(serverRoot+"view_dataType");
			//super(serverRoot+"sensorDataType.xml");
		}
		public static function getInstance():FilledDataType
		{
			if(unique==null)
				unique=new FilledDataType();
			return unique;
		}
		
	}
}