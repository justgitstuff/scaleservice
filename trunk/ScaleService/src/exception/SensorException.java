/**
 * 
 */
package exception;

/**
 * @author ÕıÕ¨÷€
 *
 */
public class SensorException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2578366780532601621L;
	
	public static final String SensorAlreadyExist="Sensor Tag Already Exist.";
	public static final String SensorNotExist="Cannot find the Sensor matches the sensorTag.";
	public static final String PrimaryKeyNotNull="You must leave the sensorID field as null if you are to add a new sensor.";
	
	/**
	 * 
	 */
	public SensorException()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public SensorException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public SensorException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SensorException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
