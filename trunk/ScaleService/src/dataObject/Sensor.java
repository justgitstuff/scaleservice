package dataObject;

import java.util.ArrayList;
import java.util.Iterator;
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
	public static Element getSensorListXML()
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
					Element sensorItem=XMLDoc.createElement("row");

					Element sensorTag=XMLDoc.createElement("sensorTag");
					sensorTag.appendChild(XMLDoc.createTextNode(e.getSensorTag()));
					
					Element sensorName=XMLDoc.createElement("sensorName");
					sensorName.appendChild(XMLDoc.createTextNode(e.getSensorName()));
					
					Element location=XMLDoc.createElement("location");
					location.appendChild(XMLDoc.createTextNode(e.getLocation()));
					
					Element manufacturer=XMLDoc.createElement("manufacturer");
					manufacturer.appendChild(XMLDoc.createTextNode(e.getManufacturer()));
					
					Element description=XMLDoc.createElement("description");
					description.appendChild(XMLDoc.createTextNode(e.getDescription()));
					
					Element memo=XMLDoc.createElement("memo");
					memo.appendChild(XMLDoc.createTextNode(e.getMemo()));
					
					sensorItem.appendChild(sensorTag);
					sensorItem.appendChild(sensorName);
					sensorItem.appendChild(location);
					sensorItem.appendChild(manufacturer);
					sensorItem.appendChild(description);
					sensorItem.appendChild(memo);
					root.appendChild(sensorItem);
				}
			}
		}
		finally
		{
			//do nothing
		}
		return root;
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
	public Element getDataTypeListXML()
	{
		DocumentBuilder db;
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
		Element root=XMLDoc.createElement("dataType");
		try
		{
			Iterator<DataType> it=dataType.iterator();
			if (it.hasNext())
			{
				for (DataType e : dataType)
				{
					Element dataTypeItem=XMLDoc.createElement("row");

					Element unit=XMLDoc.createElement("unit");
					unit.appendChild(XMLDoc.createTextNode(e.getUnit()));
					
					Element typeName=XMLDoc.createElement("typeName");
					typeName.appendChild(XMLDoc.createTextNode(e.getTypeName()));
					
					Element maxCustom=XMLDoc.createElement("maxCustom");
					maxCustom.appendChild(XMLDoc.createTextNode(e.getMaxCustom()));
					
					Element minCustom=XMLDoc.createElement("minCustom");
					minCustom.appendChild(XMLDoc.createTextNode(e.getMinCustom()));
					
					dataTypeItem.appendChild(unit);
					dataTypeItem.appendChild(typeName);
					dataTypeItem.appendChild(maxCustom);
					dataTypeItem.appendChild(minCustom);
					root.appendChild(dataTypeItem);
				}
			}
		} finally
		{
			//do nothing
		}
		return root;
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
