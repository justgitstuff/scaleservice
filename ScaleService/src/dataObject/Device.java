package dataObject;

import java.util.ArrayList;
import java.util.Iterator;
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

import exception.DeviceAndControlException;
import exception.UserException;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Device extends DOBase
{
	public static Device getDevice(String deviceTag) throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
			PersistenceManager pm = getPersistenceManager();
			try
			{
				return pm.getObjectById(Device.class, user.getNickname()+"."+deviceTag);
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
	public static List<Device> getDeviceList() throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
			PersistenceManager pm = getPersistenceManager();
			Query query = pm.newQuery(Device.class);
			query.setFilter("userNickname == un");
			query.declareParameters("String un");
			List<Device> results = (List<Device>) query.execute(user.getNickname());
			return results;
	    }
	    else
	    {
	    	throw new UserException(UserException.NotLogedIn);
	    }
	}
	@PrimaryKey
	@Persistent
	private Key deviceID;
	@Persistent
	private String userNickname;
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
	public void saveAsNew() throws DeviceAndControlException, UserException
	{
		if(Device.getDevice(this.deviceTag)==null && this.deviceID==null)
		{
			UserService userService = UserServiceFactory.getUserService();
		    User user = userService.getCurrentUser();
		    if (user != null)
		    {
		    	this.userNickname=user.getNickname();
		    	Key id = KeyFactory.createKey(Device.class.getSimpleName(),userNickname+"."+deviceTag);
		    	this.deviceID=id;
		    	PersistenceManager pm = getPersistenceManager();
				pm.makePersistent(this);
		    }
		    else
		    {
		    	throw new UserException(UserException.NotLogedIn);
		    }
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
	public Control getControl(String command,String parameter)
	{
		Control returnControl=null;
		Control iControl;
		try
		{
			Iterator<Control> it=this.control.iterator(); 
			while(it.hasNext())
			{
				iControl=it.next();
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
	public List<Control> getControl()
	{
		return control;
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
	 * @throws UserException 
	 */
	public Key getDeviceID() throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		if(user!=null)
		{
			if(deviceID==null)
				this.deviceID = KeyFactory.createKey(Device.class.getSimpleName(),userNickname+"."+deviceTag);
			return deviceID;
		}
		else
			throw new UserException(UserException.NotLogedIn);
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
	/**
	 * @return the userNickname
	 */
	public String getUserNickname()
	{
		return userNickname;
	}
	/**
	 * @param userNickname the userNickname to set
	 */
	public void setUserNickname(String userNickname)
	{
		this.userNickname = userNickname;
	}
}
