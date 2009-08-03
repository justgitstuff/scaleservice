package exception;

public class UserException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4273082889869256264L;
	public static final String NotLogedIn="You are not loged in...";
	public static final String PermissionDenied="You don't have the access...";
	/**
	 * 
	 */
	public UserException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UserException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UserException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UserException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
