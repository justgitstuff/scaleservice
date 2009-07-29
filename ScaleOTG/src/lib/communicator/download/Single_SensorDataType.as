package lib.communicator.download
{
	import lib.communicator.DownloadBase;
	public class Single_SensorDataType extends DownloadBase
	{
		public function Single_SensorDataType(sensorDataTypeID:uint)
		{
			//super(serverRoot+"SensorDataType_Single.xml");
			super(serverRoot+"sensor/getSensorDataTypeInfo.php");
			HS_list.request.sensorDataTypeID=sensorDataTypeID;
		}

	}
}