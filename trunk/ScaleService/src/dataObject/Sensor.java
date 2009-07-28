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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.appengine.api.datastore.Key;

import factory.PMF;
import factory.SensorDataFactory;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Sensor
{
	@SuppressWarnings("unchecked")
	public static Sensor getSensor(String sensorTag)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Sensor.class);
		query.setFilter("sensorTag == st");
		query.declareParameters("String st");
		List<Sensor> results = (List<Sensor>) query.execute(sensorTag);
		if (results.iterator().hasNext())
		{
			return results.iterator().next();
		}
		else
		{
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static Element getSensorXML()
	{
		DocumentBuilder db;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Sensor.class);
		try
		{
			db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		}
		Document XMLDoc=db.newDocument();
		Element root=XMLDoc.createElement("sensors");
		try
		{
			List<Sensor> results = (List<Sensor>) query.execute();
			if (results.iterator().hasNext())
			{
				for (Sensor e : results)
				{
					Element sensorItem=XMLDoc.createElement("sensor");

					Element sensorTag=XMLDoc.createElement("sensorTag");
					sensorTag.appendChild(XMLDoc.createTextNode(e.getSensorTag()));
					
					Element sensorName=XMLDoc.createElement("sensorName");
					sensorName.appendChild(XMLDoc.createTextNode(e.getSensorName()));
					
					Element location=XMLDoc.createElement("location");
					location.appendChild(XMLDoc.createTextNode(e.getLocation()));
					
					Element manufacturer=XMLDoc.createElement("manufacturer");
					manufacturer.appendChild(XMLDoc.createTextNode(e.getManufacturer()));
					
					sensorItem.appendChild(sensorTag);
					sensorItem.appendChild(sensorName);
					sensorItem.appendChild(location);
					sensorItem.appendChild(manufacturer);
					root.appendChild(sensorItem);
				}
			}
		}
		finally
		{
			pm.close();
		}
		return root;
	}
	@Persistent
	private String location;
	@Persistent
	private String manufacturer;
	@Persistent
	private List<SensorData> sensorData;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key sensorID;
	@Persistent
	private String sensorName;
	@Persistent
	private String sensorTag;
	/**
	 * @param sensorName
	 * @param location
	 * @param manufacturer
	 */
	public Sensor(String sensorTag,String sensorName, String location,
			String manufacturer)
	{
		this.sensorTag=sensorTag;
		this.sensorName = sensorName;
		this.location = location;
		this.manufacturer = manufacturer;
		this.sensorData=new ArrayList<SensorData>();
	}
	public void addSensorData(double value)
	{
		this.sensorData.add(SensorDataFactory.get().newSensorData(value));
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
	public void save()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			pm.makePersistent(this);
		} 
		finally
		{
			pm.close();
		}
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
}
