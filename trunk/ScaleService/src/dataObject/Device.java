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

import exception.DeviceAndControlException;
import factory.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Device
{
	@SuppressWarnings("unchecked")
	public static Device getDevice(String deviceTag)
	{
		Device returnDevice=null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Device.class);
		query.setFilter("deviceTag == dt");
		query.declareParameters("String dt");
		try
		{
			List<Device> results = (List<Device>) query.execute(deviceTag);
			if (results.iterator().hasNext())
			{
				returnDevice = results.iterator().next();
			}
		}finally
		{
			//noting to do
		}
		return returnDevice;
	}
	public static void save()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.close();
	}
	@SuppressWarnings("unchecked")
	public static Element getDeviceListXML()
	{
		DocumentBuilder db;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Device.class);
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
		Element root=XMLDoc.createElement("device");
		try
		{
			List<Device> results = (List<Device>) query.execute();
			if (results.iterator().hasNext())
			{
				for (Device e : results)
				{
					Element deviceItem=XMLDoc.createElement("row");

					Element deviceTag=XMLDoc.createElement("deviceTag");
					deviceTag.appendChild(XMLDoc.createTextNode(e.getDeviceTag()));
					
					Element intro=XMLDoc.createElement("intro");
					intro.appendChild(XMLDoc.createTextNode(e.getIntro()));
					
					Element currentState=XMLDoc.createElement("currentState");
					currentState.appendChild(XMLDoc.createTextNode(e.getCurrentState()));
								
					deviceItem.appendChild(deviceTag);
					deviceItem.appendChild(intro);
					deviceItem.appendChild(currentState);
					
					root.appendChild(deviceItem);
				}
			}
		}
		finally
		{
			pm.close();
		}
		return root;
	}
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key deviceID;
	@Persistent
	private String deviceTag;
	@Persistent
	private String intro;
	@Persistent
	private String currentState;
	@Persistent
	private List<Control> control;
	/**
	 * @param deviceTag
	 * @param intro
	 */
	public Device(String deviceTag, String intro)
	{
		super();
		this.deviceTag = deviceTag;
		this.intro = intro;
		this.currentState="Unknown";
		this.control=new ArrayList<Control>();
	}
	public void addControl(Control control)
	{
		this.control.add(control);
	}
	public Element getControlListXML()
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
		Element root=XMLDoc.createElement("control");
		try
		{
			Iterator<Control> it=control.iterator();
			if (it.hasNext())
			{
				for (Control e : control)
				{
					Element controItem=XMLDoc.createElement("row");

					Element command=XMLDoc.createElement("command");
					command.appendChild(XMLDoc.createTextNode(e.getCommand()));
					
					Element parameter=XMLDoc.createElement("parameter");
					parameter.appendChild(XMLDoc.createTextNode(e.getParameter()));
					
					Element action=XMLDoc.createElement("action");
					action.appendChild(XMLDoc.createTextNode(e.getAction()));
										
					controItem.appendChild(command);
					controItem.appendChild(parameter);
					controItem.appendChild(action);
					
					root.appendChild(controItem);
				}
			}
		} finally
		{
			//do nothing
		}
		return root;
	}
	public void saveAsNew() throws DeviceAndControlException
	{
		if(Device.getDevice(deviceTag)==null && this.deviceID==null)
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
		else if(Device.getDevice(deviceTag)!=null)
		{
			throw new DeviceAndControlException("Device Tag Already Exist.");
		}
		else
		{
			throw new DeviceAndControlException("You must leave the DeviceID field as null if you are to add a new device.");
		}
	}
	/**
	 * @return the deviceTag
	 */
	public String getDeviceTag()
	{
		return deviceTag;
	}
	/**
	 * @return the description
	 */
	public String getIntro()
	{
		return this.intro;
	}
	/**
	 * @return the deviceID
	 */
	public Key getDeviceID()
	{
		return deviceID;
	}
	/**
	 * @return the currentState
	 */
	public String getCurrentState()
	{
		return currentState;
	}
	/**
	 * @param deviceTag the deviceTag to set
	 */
	public void setDeviceTag(String deviceTag)
	{
		this.deviceTag = deviceTag;
	}
	/**
	 * @param intro the description to set
	 */
	public void setIntro(String intro)
	{
		this.intro = intro;
	}
	/**
	 * @param deviceID the deviceID to set
	 */
	public void setDeviceID(Key deviceID)
	{
		this.deviceID = deviceID;
	}
	/**
	 * @param currentState the currentState to set
	 */
	public void setCurrentState(String currentState)
	{
		this.currentState = currentState;
	}
}
