/**
 * 
 */
package exception;

/**
 * @author ÕıÕ¨÷€
 *
 */
public class DataTypeException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5320464451376996359L;

	public static final String DataTypeNotFonund="Can not find the data type matches the given type name.";
	public static final String TypeNameAlreadyExist="The type name is already exist in the database.You need to provide a different type name.";
	public static final String TypeNameNotExist="Can't Find data type matches the typeName";
	public static final String PrimaryKeyNotNull="You must leave the dataTypeID field as null if you are to add a new data type.";
	/**
	 * 
	 */
	public DataTypeException()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public DataTypeException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public DataTypeException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataTypeException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
