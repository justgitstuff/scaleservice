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

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Operation extends DOBase
{
	public static Operation getOperation(Key controlID,Key dataTypeID) throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
			PersistenceManager pm = getPersistenceManager();
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
			PersistenceManager pm = getPersistenceManager();
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
	@SuppressWarnings("unchecked")
	public static List<Operation> getOperationList(String typeName) throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
			PersistenceManager pm = getPersistenceManager();
			Query query = pm.newQuery(Operation.class);
			query.setFilter("userNickname == un");
			query.declareParameters("String un");
			List<Operation> results = (List<Operation>) query.execute(user.getNickname());
			List<Operation> afterFilter=new ArrayList<Operation>();
			Iterator<Operation> it=results.iterator();
			Operation iOperation;
			while(it.hasNext())
			{
				iOperation=it.next();
				if(iOperation.getDataType().getTypeName().equals(typeName))
				{
					afterFilter.add(iOperation);
				}
			}
			return afterFilter;
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
		    	PersistenceManager pm = getPersistenceManager();
				pm.makePersistent(this);
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
	public void deleteThis()
	{
		PersistenceManager pm = getPersistenceManager();
		pm.deletePersistent(this);
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
	 * @return the controlID
	 */
	public Key getControlID()
	{
		return controlID;
	}
	public Control getControl()
	{
		PersistenceManager pm = getPersistenceManager();
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
		PersistenceManager pm = getPersistenceManager();
		return pm.getObjectById(DataType.class, dataTypeID);
	}
	/**
	 * @return the userNickname
	 */
	public String getUserNickname()
	{
		return userNickname;
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
	 * @param userNickname the userNickname to set
	 */
	public void setUserNickname(String userNickname)
	{
		this.userNickname = userNickname;
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
	public static List<Operation> findAllToDo(String userNickname)
	{
		List<Operation> allToDo=new ArrayList<Operation>();
		try
		{
			List<DataType> dataType = (List<DataType>) DataType.getDataTypeList(userNickname);//获取该用户的所有数据类型
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
	public static List<Control> findAllControl() throws UserException
	{
		List<Operation> fao=findAllToDo();
		List<Control> ret=new ArrayList<Control>();
		Iterator<Operation> it=fao.iterator();
		while(it.hasNext())
		{
			ret.add(it.next().getControl());
		}
		return ret;
	}
	public static List<Control> findAllControl(String userNickname)
	{
		List<Operation> fao=findAllToDo(userNickname);//找出所有会被执行的Operation
		List<Control> ret=new ArrayList<Control>();
		Iterator<Operation> it=fao.iterator();
		while(it.hasNext())
		{
			ret.add(it.next().getControl());
		}
		return ret;
	}
	public boolean equals(Operation o)
	{
		if(o.controlID.equals(this.controlID) && o.dataTypeID.equals(this.dataTypeID))
			return true;
		else
			return false;
	}
}
