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

import com.google.appengine.api.datastore.Key;

import exception.DeviceAndControlException;
import factory.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
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
	@SuppressWarnings("unchecked")
	public static List<Device> getDeviceList()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Device.class);
		List<Device> results = (List<Device>) query.execute();
		return results;
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
	public void addControl(Control control) throws DeviceAndControlException
	{
		boolean controlExist=false;
		Control cur=null;
		Iterator<Control> it=this.control.iterator();
		while(it.hasNext())
		{
			cur=it.next();
			if(cur.getCommand().equalsIgnoreCase(control.getCommand()))
			{
				if(cur.getParameter().equalsIgnoreCase(control.getParameter()))
				{
					controlExist=true;
					break;
				}
			}
		}
		if(controlExist)
			throw new DeviceAndControlException(DeviceAndControlException.ControlAlreadyExist);
		else
			this.control.add(control);
	}
	public List<Control> getControl()
	{
		return control;
	}
	@SuppressWarnings("unchecked")
	public Control getControl(String command,String parameter)
	{
		Control returnControl=null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Control.class);
		Control iControl;
		try
		{
			List<Control> results = (List<Control>) query.execute();
			Iterator<Control> it=results.iterator(); 
			while(it.hasNext())
			{
				iControl=it.next();
				iControl.setDevice(pm.getObjectById(Device.class, iControl.getDevice().getDeviceID()));
				if(iControl.getDevice().equals(this) && iControl.getCommand().equals(command) && iControl.getParameter().equals(parameter))
				{
					returnControl=iControl;
					break;
				}
			}
		}finally
		{
			//noting to do
		}
		return returnControl;
	}
	public void saveAsNew() throws DeviceAndControlException
	{
		if(Device.getDevice(deviceTag)==null && this.deviceID==null)
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			pm.makePersistent(this);
		}
		else if(this.deviceID!=null)
		{
			throw new DeviceAndControlException(DeviceAndControlException.PrimaryKeyNotNull);
		}
		else
		{
			throw new DeviceAndControlException(DeviceAndControlException.DeviceAlreadyExist);
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
	public boolean equals(Device d)
	{
		if(d.deviceTag.equals(this.deviceTag))
			return true;
		else
			return false;
	}
}
