package dataObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import util.Direction;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import exception.OperationException;
import exception.UserException;
import factory.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Operation
{
	public static Operation getOperation(Key controlID,Key dataTypeID) throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try
			{
				return pm.getObjectById(Operation.class, user.getNickname()+"."+KeyFactory.keyToString(controlID)+"/"+KeyFactory.keyToString(dataTypeID));
			}catch(JDOObjectNotFoundException e)
			{
				return null;
			}
	    }
	    else
	    {
	    	throw new UserException(UserException.NotLogedIn);
	    }
	}
	@SuppressWarnings("unchecked")
	public static List<Operation> getOperationList() throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(Operation.class);
			query.setFilter("userNickname == un");
			query.declareParameters("String un");
			List<Operation> results = (List<Operation>) query.execute(user.getNickname());
			return results;
	    }
	    else
	    {
	    	throw new UserException(UserException.NotLogedIn);
	    }
	}
	@PrimaryKey
	@Persistent
	private Key operationID;
	@Persistent
	private String userNickname;
	@Persistent
	private Key controlID;
	@Persistent
	private Key dataTypeID;
	@Persistent
	private Direction direction;
	//private Control control;
	//private DataType targetDataType;
	/**
	 * @param control
	 * @param targetDataType
	 * @param direction
	 */
	public Operation(Key controlID,Key dataTypeID,
			Direction direction)
	{
		super();
		this.controlID = controlID;
		this.dataTypeID = dataTypeID;
		this.direction = direction;
	}
	public void saveAsNew() throws OperationException, UserException
	{
		if(Operation.getOperation(controlID, dataTypeID)==null && operationID==null)
		{
			UserService userService = UserServiceFactory.getUserService();
		    User user = userService.getCurrentUser();
		    if (user != null)
		    {
		    	this.userNickname=user.getNickname();
		    	Key id = KeyFactory.createKey(Operation.class.getSimpleName(),userNickname+"."+KeyFactory.keyToString(controlID)+"/"+KeyFactory.keyToString(dataTypeID));
		    	this.operationID=id;
		    	PersistenceManager pm = PMF.get().getPersistenceManager();
				pm.makePersistent(this);
				pm.close();
		    }
		    else
		    	throw new UserException(UserException.NotLogedIn);
		}
		else if(operationID!=null)
		{
			throw new OperationException(OperationException.PrimaryKeyNotNull);
		}
		else
		{
			throw new OperationException(OperationException.OperationAlreadyExist);
		}
	}
	/**
	 * @return the operationID
	 * @throws UserException 
	 */
	public Key getOperationID() throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		if(user!=null)
		{
			if(this.operationID==null)
				this.operationID=KeyFactory.createKey(Operation.class.getSimpleName(),userNickname+"."+KeyFactory.keyToString(controlID)+"/"+KeyFactory.keyToString(dataTypeID));
			return operationID;
		}
		else
			throw new UserException(UserException.NotLogedIn);
	}
	/**
	 * @return the direction
	 */
	public Direction getDirection()
	{
		return direction;
	}
	/**
	 * @param operationID the operationID to set
	 */
	public void setOperationID(Key operationID)
	{
		this.operationID = operationID;
	}
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}
	public boolean equals(Operation o)
	{
		if(o.controlID.equals(this.controlID) && o.dataTypeID.equals(this.dataTypeID))
			return true;
		else
			return false;
	}
	/**
	 * @return the controlID
	 */
	public Key getControlID()
	{
		return controlID;
	}
	public Control getControl()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		return pm.getObjectById(Control.class, controlID);
	}
	/**
	 * @return the dataTypeID
	 */
	public Key getDataTypeID()
	{
		return dataTypeID;
	}
	public DataType getDataType()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		return pm.getObjectById(DataType.class, dataTypeID);
	}
	/**
	 * @param controlID the controlID to set
	 */
	public void setControlID(Key controlID)
	{
		this.controlID = controlID;
	}
	/**
	 * @param dataTypeID the dataTypeID to set
	 */
	public void setDataTypeID(Key dataTypeID)
	{
		this.dataTypeID = dataTypeID;
	}
	/**
	 * 获取所有DataType中的解决方案，写入一个List中
	 * @return
	 * @throws UserException 
	 */
	public static List<Operation> findAllToDo() throws UserException
	{
		List<Operation> allToDo=new ArrayList<Operation>();
		try
		{
			List<DataType> dataType = (List<DataType>) DataType.getDataType();
			Iterator<DataType> it=dataType.iterator();
			while(it.hasNext())
			{
				allToDo.addAll(it.next().findToDo());
			}
		}finally
		{
			//noting to do
		}
		return allToDo;
	}
	/**
	 * @return the userNickname
	 */
	public String getUserNickname()
	{
		return userNickname;
	}
	/**
	 * @param userNickname the userNickname to set
	 */
	public void setUserNickname(String userNickname)
	{
		this.userNickname = userNickname;
	}
}
