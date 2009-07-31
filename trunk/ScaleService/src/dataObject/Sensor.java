package dataObject;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import exception.SensorException;
import factory.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Sensor
{
	@SuppressWarnings("unchecked")
	public static Sensor getSensor(String sensorTag)
	{
		Sensor returnSensor=null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Sensor.class);
		query.setFilter("sensorTag == st");
		query.declareParameters("String st");
		try
		{
			List<Sensor> results = (List<Sensor>) query.execute(sensorTag);
			if (results.iterator().hasNext())
			{
				returnSensor = results.iterator().next();
			}
		}finally
		{
			//noting to do
		}
		return returnSensor;
	}
	@SuppressWarnings("unchecked")
	public static List<Sensor> getSensor()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Sensor.class);
		List<Sensor> results = (List<Sensor>) query.execute();
		return results;
	}
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key sensorID;
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
	private List<DataType> dataType;
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
		this.dataType=new ArrayList<DataType>();
	}
	/**
	 * @param sensorName
	 * @param location
	 * @param manufacturer
	 */
	
	public void addDataType(DataType dataType)
	{
		this.dataType.add(dataType);
	}
	public List<DataType> getDataType()
	{
		return dataType;
	}
	public void saveAsNew() throws SensorException
	{
		if(Sensor.getSensor(this.sensorTag)==null && this.sensorID==null)
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			pm.makePersistent(this);
			pm.close();
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
	 */
	public Key getSensorID()
	{
		return sensorID;
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
