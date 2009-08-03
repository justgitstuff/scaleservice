package dataObject;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import exception.DataTypeException;
import exception.SensorException;
import exception.UserException;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Sensor extends DOBase
{
	public static Sensor getSensor(String sensorTag) throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
			PersistenceManager pm = getPersistenceManager();
			try
			{
				return pm.getObjectById(Sensor.class, user.getNickname()+"."+sensorTag);
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
	public static List<Sensor> getSensor() throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
			PersistenceManager pm = getPersistenceManager();
			Query query = pm.newQuery(Sensor.class);
			query.setFilter("userNickname == un");
			query.declareParameters("String un");
			List<Sensor> results = (List<Sensor>) query.execute(user.getNickname());
			return results;
	    }
	    else
	    {
	    	throw new UserException(UserException.NotLogedIn);
	    }
	}
	@PrimaryKey
	@Persistent
	private Key sensorID;
	@Persistent
	private String userNickname;
	@Persistent
	private String sensorTag;
	@Persistent
	private String sensorName;
	@Persistent
	private String location;
	@Persistent
	private String manufacturer;
	@Persistent
	private String description;
	@Persistent
	private String memo;
	@Persistent
	private List<Key> dataTypeID;
	/**
	 * @param location
	 * @param manufacturer
	 * @param sensorName
	 * @param description
	 * @param memo
	 * @param sensorTag
	 */
	public Sensor(String sensorTag, String sensorName,String location, String manufacturer,
			String description, String memo)
	{
		super();
		this.location = location;
		this.manufacturer = manufacturer;
		this.sensorName = sensorName;
		this.description = description;
		this.memo = memo;
		this.sensorTag = sensorTag;
		this.dataTypeID=new ArrayList<Key>();
	}
	/**
	 * 添加一个dataType，如果该TypeName已存在，则覆盖之
	 * @param sensorName
	 * @param location
	 * @param manufacturer
	 * @throws UserException 
	 * @throws DataTypeException 
	 */
	
	public void addDataType(DataType dataType) throws UserException, DataTypeException
	{
		dataTypeID.add(dataType.getDataTypeID());
	}
	/**
	 * 自动加上用户属性，储存为新的传感器
	 * @throws SensorException
	 * @throws UserException
	 */
	public void saveAsNew() throws SensorException, UserException
	{
		if(Sensor.getSensor(this.sensorTag)==null && this.sensorID==null)
		{
			UserService userService = UserServiceFactory.getUserService();
		    User user = userService.getCurrentUser();
		    if (user != null)
		    {
		    	this.userNickname=user.getNickname();
		    	Key id = KeyFactory.createKey(Sensor.class.getSimpleName(),userNickname+"."+sensorTag);
		    	this.sensorID=id;
		    	PersistenceManager pm = getPersistenceManager();
				pm.makePersistent(this);
		    }
		    else
		    {
		    	throw new UserException(UserException.NotLogedIn);
		    }
		}
		else if(this.sensorID!=null)
		{
			throw new SensorException(SensorException.PrimaryKeyNotNull);
		}
		else
		{
			throw new SensorException(SensorException.SensorAlreadyExist);
		}
	}
	public List<Key> getDataType()
	{
		return dataTypeID;
	}
	/**
	 * @return the userNickname
	 */
	public String getUserNickname()
	{
		return userNickname;
	}
	/**
	 * @return the location
	 */
	public String getLocation()
	{
		return location;
	}
	/**
	 * @return the manufacturer
	 */
	public String getManufacturer()
	{
		return manufacturer;
	}
	/**
	 * @return the sensorID
	 * @throws UserException 
	 */
	public Key getSensorID() throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		if(user!=null)
		{
			if(this.sensorID==null)
				this.sensorID=KeyFactory.createKey(Sensor.class.getSimpleName(),userNickname+"."+sensorTag);
			return sensorID;
		}
		else
			throw new UserException(UserException.NotLogedIn);
	}
	/**
	 * @return the sensorName
	 */
	public String getSensorName()
	{
		return sensorName;
	}
	/**
	 * @return the sensorTag
	 */
	public String getSensorTag()
	{
		return sensorTag;
	}
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @return the memo
	 */
	public String getMemo()
	{
		return memo;
	}
	/**
	 * @param userNickname the userNickname to set
	 */
	public void setUserNickname(String userNickname)
	{
		this.userNickname = userNickname;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(String manufacturer)
	{
		this.manufacturer = manufacturer;
	}
	/**
	 * @param sensorID the sensorID to set
	 */
	public void setSensorID(Key sensorID)
	{
		this.sensorID = sensorID;
	}
	/**
	 * @param sensorName the sensorName to set
	 */
	public void setSensorName(String sensorName)
	{
		this.sensorName = sensorName;
	}
	/**
	 * @param sensorTag the sensorTag to set
	 */
	public void setSensorTag(String sensorTag)
	{
		this.sensorTag = sensorTag;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo)
	{
		this.memo = memo;
	}
	public boolean equals(Sensor s)
	{
		if(s.sensorTag==this.sensorTag)
			return true;
		else
			return false;
	}
}
