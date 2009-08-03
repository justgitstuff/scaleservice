package exception;

public class OperationException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7202818268969767492L;
	public static final String PrimaryKeyNotNull="You must leave the operationID field as null if you are to add a new operation.";
	public static final String OperationAlreadyExist="This combination of control and target dataType Already Exists in the database.";
	public static final String OperationNotExist="Cannot find Operation matches the given control and dataType";
	public static final String OperationIDNotExist="The provided operationID is not valid";
	public static final String OperationIsNew="The operation is new, and you must invoke PMF.saveAndClose to save it before adding it to a scene.";
	public static final String OperationNotComplete="There is at least one require field is null in the given operation";
	public OperationException()
	{
		// TODO Auto-generated constructor stub
	}

	public OperationException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public OperationException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public OperationException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
