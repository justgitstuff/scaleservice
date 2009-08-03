package exception;

public class SceneException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5205325088286351191L;
	public static final String SceneAlreadyExist="The scane name already exists in the database";
	public static final String SceneNotExist="Cannot find the scene with the given scene name";
	public static final String PrimaryKeyNotNull="You need to leave the sceneID field blank in order to save it as new";
	/**
	 * 
	 */
	public SceneException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SceneException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public SceneException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public SceneException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
