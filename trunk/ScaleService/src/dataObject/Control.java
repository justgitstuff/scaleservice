package dataObject;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Control
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key controlID;
	@Persistent
	private String command;
	@Persistent
	private String parameter;
	@Persistent
	private String action;//describe the command
	/**
	 * @param command
	 * @param parameter
	 * @param action
	 */
	public Control(String command, String parameter, String action)
	{
		super();
		this.command = command;
		this.parameter = parameter;
		this.action = action;
	}
	/**
	 * @return the controlID
	 */
	public Key getControlID()
	{
		return controlID;
	}
	/**
	 * @return the command
	 */
	public String getCommand()
	{
		return command;
	}
	/**
	 * @return the parameter
	 */
	public String getParameter()
	{
		return parameter;
	}
	/**
	 * @return the action
	 */
	public String getAction()
	{
		return action;
	}
	/**
	 * @param controlID the controlID to set
	 */
	public void setControlID(Key controlID)
	{
		this.controlID = controlID;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(String command)
	{
		this.command = command;
	}
	/**
	 * @param parameter the parameter to set
	 */
	public void setParameter(String parameter)
	{
		this.parameter = parameter;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action)
	{
		this.action = action;
	}
	
}
