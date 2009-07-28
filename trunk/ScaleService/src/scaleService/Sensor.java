package scaleService;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Sensor
{
	/**
	 * @param sensorID
	 * @param sensorName
	 * @param location
	 * @param manufacturer
	 */
	public Sensor(String sensorName, String location,
			String manufacturer)
	{
		this.sensorName = sensorName;
		this.location = location;
		this.manufacturer = manufacturer;
	}
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long sensorID;
	@Persistent
	private String sensorName;
	@Persistent
	private String location;
	@Persistent
	private String manufacturer;
	/**
	 * @return the sensorID
	 */
	
	public Long getSensorID()
	{
		return sensorID;
	}
	/**
	 * @param sensorID the sensorID to set
	 */
	public void setSensorID(Long sensorID)
	{
		this.sensorID = sensorID;
	}
	/**
	 * @return the sensorName
	 */
	public String getSensorName()
	{
		return sensorName;
	}
	/**
	 * @param sensorName the sensorName to set
	 */
	public void setSensorName(String sensorName)
	{
		this.sensorName = sensorName;
	}
	/**
	 * @return the location
	 */
	public String getLocation()
	{
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	/**
	 * @return the manufacturer
	 */
	public String getManufacturer()
	{
		return manufacturer;
	}
	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(String manufacturer)
	{
		this.manufacturer = manufacturer;
	}
	
}
