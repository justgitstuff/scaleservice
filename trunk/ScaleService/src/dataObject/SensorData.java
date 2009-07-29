package dataObject;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SensorData
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key sensorDataID;
	@Persistent
	private double value;
	@Persistent
	private Date time;
	/**
	 * @param d
	 * @param date
	 */
	public SensorData(double d, Date date)
	{
		this.value = d;
		this.time = date;
	}
	/**
	 * @return the sensorDataID
	 */
	public Key getSensorDataID()
	{
		return sensorDataID;
	}
	/**
	 * @param sensorDataID the sensorDataID to set
	 */
	public void setSensorDataID(Key sensorDataID)
	{
		this.sensorDataID = sensorDataID;
	}
	/**
	 * @return the data
	 */
	public double getValue()
	{
		return value;
	}
	/**
	 * @param data the data to set
	 */
	public void setValue(double value)
	{
		this.value = value;
	}
	/**
	 * @return the time
	 */
	public Date getTime()
	{
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(Date time)
	{
		this.time = time;
	}
	
}
