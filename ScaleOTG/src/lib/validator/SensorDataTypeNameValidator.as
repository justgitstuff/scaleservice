package lib.validator
{
	import lib.communicator.download.AllDataType;
	
	import mx.validators.Validator;
	import mx.validators.ValidationResult;

	public class SensorDataTypeNameValidator extends Validator
	{
		private var results:Array;
		public function SensorDataTypeNameValidator()
		{
			super();
		}
		override protected function doValidation(value:Object):Array
		{
			var inputValue:String = value.toString();
			results=[];
			results=super.doValidation(inputValue);
			if(results.length>0)
				return results;
			var ADR:AllDataType=AllDataType.getInstance();
			if(!ADR.rcvRes)//尚未获取DataType
			{
				results.push(new ValidationResult(true, null, "尚未载入数据类型信息", "无法载入数据类型名列表"));
			}
			else
			{
				var searchResult:XML=ADR.recordXML.row.(@typeName==inputValue)[0];
				if(searchResult!=null)
				{
					results.push(new ValidationResult(true, null, "名称已存在", "该数据类型名称已存在"));
				}
			}
			return results;	
		}
	}
}