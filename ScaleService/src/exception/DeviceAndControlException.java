package exception;

public class DeviceAndControlException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1478834017913970826L;
	public static final String ControlAlreadyExist="the combinable of the command and parameter already exists in this device(The case of the letter is not sensitive)";
	public static final String ControlNotExist="Can't find contol with the given command and parameter belonging to the deviceTag";
	public static final String ControlIsNew="This is a new control,you must save it as new before adding it to a scene";
	public static final String DeviceAlreadyExist="Device Tag Already Exist.";
	public static final String DeviceNotExist="Can't find device matches the deviceTag";
	public static final String PrimaryKeyNotNull="You must leave the DeviceID field as null if you are to add a new device.";
	//public static final String OperationPrimaryKeyNotNull=
	public DeviceAndControlException()
	{
		// TODO Auto-generated constructor stub
	}

	public DeviceAndControlException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DeviceAndControlException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public DeviceAndControlException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
