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

import exception.DataTypeException;
import exception.UserException;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DataType extends DOBase
{
	/**
	 * 查找该属于用户的，指定数据类型名称的数据类型
	 * @param typeName
	 * @return
	 * @throws UserException
	 */
	public static DataType getDataType(String typeName) throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    DataType d=null;
		if (user != null)
	    {
			PersistenceManager pm = getPersistentManager();
			try
			{
				d = pm.getObjectById(DataType.class, user.getNickname()+"."+typeName);
			}catch(JDOObjectNotFoundException e)
			{
				d = null;
			}
	    }
	    else
	    {
	    	throw new UserException(UserException.NotLogedIn);
	    }
		return d;
	}
	@SuppressWarnings("unchecked")
	public static List<DataType> getDataType() throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
			PersistenceManager pm = getPersistentManager();
			Query query = pm.newQuery(DataType.class);
			query.setFilter("userNickname == un");
			query.declareParameters("String un");
			List<DataType> results = (List<DataType>) query.execute(user.getNickname());
			return results;
	    }
	    else
	    {
	    	throw new UserException(UserException.NotLogedIn);
	    }
	}
	public static void update(String unit, String typeName, Double maxCustom,Double minCustom) throws DataTypeException, UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		if (user != null)
	    {
			PersistenceManager pm = getPersistentManager();
			try
			{
				DataType d=pm.getObjectById(DataType.class, user.getNickname()+"."+typeName);
				d.setUnit(unit);
				d.setTypeName(typeName);
				d.setMaxCustom(maxCustom);
				d.setMinCustom(minCustom);
			}catch(JDOObjectNotFoundException e)
			{
				throw new DataTypeException(DataTypeException.DataTypeNotFonund);
			}finally
			{
				pm.close();
			}
	    }
	    else
	    {
	    	throw new UserException(UserException.NotLogedIn);
	    }
	}
	@PrimaryKey
	@Persistent
	private Key dataTypeID;
	@Persistent
	private String unit;
	@Persistent
	private String userNickname;
	@Persistent
	private String typeName;
	@Persistent
	private Double maxCustom;
	@Persistent
	private Double minCustom;
	@Persistent
	private List<SensorData> sensorData;
	/**
	 * @param unit
	 * @param typeName
	 * @param maxCustom
	 * @param minCustom
	 */
	public DataType(String unit, String typeName, Double maxCustom,
			Double minCustom)
	{
		super();
		this.unit = unit;
		this.typeName = typeName;
		this.maxCustom = maxCustom;
		this.minCustom = minCustom;
		this.sensorData=new ArrayList<SensorData>();
	}
	public void saveAsNew() throws UserException, DataTypeException
	{
		if(DataType.getDataType(this.typeName)==null)
		{
			UserService userService = UserServiceFactory.getUserService();
		    User user = userService.getCurrentUser();
		    if (user != null)
		    {
		    	this.userNickname=user.getNickname();
		    	this.dataTypeID=getDataTypeID();
		    	PersistenceManager pm = getPersistentManager();
				pm.makePersistent(this);
		    }
		    else
		    {
		    	throw new UserException(UserException.NotLogedIn);
		    }
		}
		else
		{
			throw new DataTypeException(DataTypeException.TypeNameAlreadyExist);
		}
	}
	public void addSensorData(SensorData sensorData)
	{
		this.sensorData.add(sensorData);
	}
	
	/**
	 * @return the sensorData
	 */
	public List<SensorData> getSensorData()
	{
		return sensorData;
	}
	/**
	 * @return the dataTypeID
	 * @throws UserException 
	 */
	public Key getDataTypeID() throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		if(user!=null)
		{
			if(this.dataTypeID==null)
				this.dataTypeID=KeyFactory.createKey(DataType.class.getSimpleName(), user.getNickname()+"."+typeName);
			return dataTypeID;
		}
		else
			throw new UserException(UserException.NotLogedIn);
	}
	/**
	 * @return the unit
	 */
	public String getUnit()
	{
		return unit;
	}
	/**
	 * @return the typeName
	 */
	public String getTypeName()
	{
		return typeName;
	}
	/**
	 * @return the maxCustom
	 */
	public Double getMaxCustom()
	{
		return maxCustom;
	}
	/**
	 * @return the minCustom
	 */
	public Double getMinCustom()
	{
		return minCustom;
	}
	public void setAll(String unit, String typeName, Double maxCustom,Double minCustom)
	{
		this.unit = unit;
		this.typeName = typeName;
		this.maxCustom = maxCustom;
		this.minCustom = minCustom;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}
	/**
	 * @param maxCustom the maxCustom to set
	 */
	public void setMaxCustom(Double maxCustom)
	{
		this.maxCustom = maxCustom;
	}
	/**
	 * @param minCustom the minCustom to set
	 */
	public void setMinCustom(Double minCustom)
	{
		this.minCustom = minCustom;
	}
	public boolean equals(DataType d)
	{
		if(d.typeName.equals(this.typeName))
			return true;
		else
			return false;
	}
	/**
	 * 查找当前数据类型的值是否符合要求，如果不符合要求的话则返回相应的解决方案
	 * @return List<Operation>
	 */
	@SuppressWarnings("unchecked")
	public List<Operation> findToDo()
	{
		SensorData lastSensorData=getLastSensorData();
		List<Operation> todo=new ArrayList<Operation>();
		Operation iOperation;
		if(lastSensorData.getValue()>this.maxCustom)//如果当前值超过了预定值
		{
			PersistenceManager pm = getPersistentManager();
			Query query = pm.newQuery(Operation.class);
			try
			{
				List<Operation> results = (List<Operation>) query.execute();
				Iterator<Operation> it=results.iterator();
				while(it.hasNext())
				{
					iOperation=it.next();
					if(iOperation.getDataType().equals(this) && iOperation.getDirection().equals(Direction.Down))
					{
						todo.add(iOperation);
					}
				}
			}finally
			{
				//noting to do
			}
		}
		else if(lastSensorData.getValue()<this.minCustom)
		{
			PersistenceManager pm = getPersistentManager();
			Query query = pm.newQuery(Operation.class);
			try
			{
				List<Operation> results = (List<Operation>) query.execute();
				Iterator<Operation> it=results.iterator();
				while(it.hasNext())
				{
					iOperation=it.next();
					if(iOperation.getDataType().equals(this) && iOperation.getDirection().equals(Direction.Up))
					{
						todo.add(iOperation);
					}
				}
			}finally
			{
				//noting to do
			}
		}
		return todo;
	}
	/**
	 * 获取当前数据类型的最后一次数据
	 * @return SensorData
	 */
	public SensorData getLastSensorData()
	{
		Iterator<SensorData> it=sensorData.iterator();
		SensorData lastSensorData=null;
		while(it.hasNext())
		{
			lastSensorData=it.next();
		}
		return lastSensorData;
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
