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
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key sensorID;
	@Persistent
	private String sensorName;
	@Persistent
	private String location;
	@Persistent
	private String manufacturer;
	@Persistent
	private List<SensorData> sensorData;
	/**
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
		this.sensorData=new ArrayList<SensorData>();
		this.sensorData.add(SensorDataFactory.get().newSensorData(1.22));
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
	/**
	 * @return the sensorID
	 */
	public Key getSensorID()
	{
		return sensorID;
	}
	/**
	 * @param sensorID the sensorID to set
	 */
	public void setSensorID(Key sensorID)
	{
		this.sensorID = sensorID;
	}
	@SuppressWarnings("unchecked")
	public static Element getSensorXML()
	{
		DocumentBuilder db;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Sensor.class);
		//query.setFilter("manufacturer == manu");
		//query.declareParameters("String manu");
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
					Element dataItem=XMLDoc.createElement("row");

					Element value=XMLDoc.createElement("sensorName");
					value.appendChild(XMLDoc.createTextNode(e.getSensorName()));
					
					Element location=XMLDoc.createElement("location");
					location.appendChild(XMLDoc.createTextNode(e.getLocation()));
					
					Element manufacturer=XMLDoc.createElement("manufacturer");
					manufacturer.appendChild(XMLDoc.createTextNode(e.getManufacturer()));
					
					dataItem.appendChild(value);
					dataItem.appendChild(location);
					dataItem.appendChild(manufacturer);
					root.appendChild(dataItem);
				}
			}
		} finally
		{
			pm.close();
		}
		return root;
	}
}
