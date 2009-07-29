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

import exception.DataTypeException;
import factory.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DataType
{
	@SuppressWarnings("unchecked")
	public static DataType getDataType(String typeName)
	{
		DataType returnDataType=null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(DataType.class);
		query.setFilter("typeName == tn");
		query.declareParameters("String tn");
		try
		{
			List<DataType> results = (List<DataType>) query.execute(typeName);
			if (results.iterator().hasNext())
			{
				returnDataType = results.iterator().next();
			}
		}finally
		{
			//noting to do
		}
		return returnDataType;
	}
	public static void save()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.close();
	}
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key dataTypeID;
	@Persistent
	private String unit;
	@Persistent
	private String typeName;
	@Persistent
	private String maxCustom;
	@Persistent
	private String minCustom;
	@Persistent
	private List<SensorData> sensorData;
	/**
	 * @param unit
	 * @param typeName
	 * @param maxCustom
	 * @param minCustom
	 */
	public DataType(String unit, String typeName, String maxCustom,
			String minCustom)
	{
		super();
		this.unit = unit;
		this.typeName = typeName;
		this.maxCustom = maxCustom;
		this.minCustom = minCustom;
		this.sensorData=new ArrayList<SensorData>();
	}
	public void addSensorData(SensorData sensorData)
	{
		this.sensorData.add(sensorData);
	}
	public Element getSensorDataListXML()
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
		Element root=XMLDoc.createElement("sensorData");
		try
		{
			Iterator<SensorData> it=sensorData.iterator();
			if (it.hasNext())
			{
				for (SensorData e : sensorData)
				{
					Element sensorDataItem=XMLDoc.createElement("row");

					Element value=XMLDoc.createElement("value");
					value.appendChild(XMLDoc.createTextNode(String.valueOf(e.getValue())));
					
					Element time=XMLDoc.createElement("time");
					time.appendChild(XMLDoc.createTextNode(e.getTime().toString()));
					
					sensorDataItem.appendChild(value);
					sensorDataItem.appendChild(time);
					root.appendChild(sensorDataItem);
				}
			}
		} finally
		{
			//do nothing
		}
		return root;
	}
	public void saveAsNew() throws DataTypeException
	{
		if(DataType.getDataType(this.typeName)==null && this.dataTypeID==null)
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try
			{
				pm.makePersistent(this);
			} finally
			{
				pm.close();
			}
		}
		else if(DataType.getDataType(this.typeName)!=null)
		{
			throw new DataTypeException("Data Type Name Already Exist.");
		}
		else
		{
			throw new DataTypeException("You must leave the dataTypeID field as null if you are to add a new data type.");
		}
	}
	/**
	 * @return the dataTypeID
	 */
	public Key getDataTypeID()
	{
		return dataTypeID;
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
	public String getMaxCustom()
	{
		return maxCustom;
	}
	/**
	 * @return the minCustom
	 */
	public String getMinCustom()
	{
		return minCustom;
	}
	/**
	 * @param dataTypeID the dataTypeID to set
	 */
	public void setDataTypeID(Key dataTypeID)
	{
		this.dataTypeID = dataTypeID;
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
	public void setMaxCustom(String maxCustom)
	{
		this.maxCustom = maxCustom;
	}
	/**
	 * @param minCustom the minCustom to set
	 */
	public void setMinCustom(String minCustom)
	{
		this.minCustom = minCustom;
	}
}
