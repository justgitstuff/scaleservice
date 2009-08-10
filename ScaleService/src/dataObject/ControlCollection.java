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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import exception.DeviceAndControlException;
import exception.UserException;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ControlCollection extends DOBase
{
	@SuppressWarnings("unchecked")
	public static ControlCollection getControlCollection() throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    ControlCollection d=null;
		if (user != null)
	    {
			PersistenceManager pm = getPersistenceManager();
			Query query = pm.newQuery(ControlCollection.class);
			query.setFilter("userNickname == un");
			query.declareParameters("String un");
			List<ControlCollection> dl = (List<ControlCollection>) query.execute(user.getNickname());
			if(dl.iterator().hasNext())
			{
				d=dl.iterator().next();
				if(d.controlList==null)
					d.controlList=new ArrayList<Key>();
			}
			else
			{
				d = new ControlCollection();
				pm.makePersistent(d);
			}
	    }
	    else
	    {
	    	throw new UserException(UserException.NotLogedIn);
	    }
		return d;
	}
	@SuppressWarnings("unchecked")
	public static ControlCollection getControlCollection(String userNickname)
	{
		PersistenceManager pm = getPersistenceManager();
		Query query = pm.newQuery(ControlCollection.class);
		query.setFilter("userNickname == un");
		query.declareParameters("String un");
		List<ControlCollection> dl = (List<ControlCollection>) query.execute(userNickname);
		if(dl.iterator().hasNext())
			return dl.iterator().next();
		else
			return null;
	}
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key controlCollectionID;
	@Persistent
	private String userNickname;
	@Persistent
	private List<Key> controlList;
	public ControlCollection()
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
	    	this.controlCollectionID=KeyFactory.createKey(ControlCollection.class.getSimpleName(), user.getNickname());
	    	this.userNickname=user.getNickname();
	    	controlList=new ArrayList<Key>();
	    }
	    
	}
	/**
	 * 将Control插入待执行列表中
	 * @param control
	 * @return
	 * @throws DeviceAndControlException 
	 */
	public boolean addControl(Control control) throws DeviceAndControlException
	{
		PersistenceManager pm = getPersistenceManager();
		if(control==null)
			throw new DeviceAndControlException(DeviceAndControlException.ControlNotExist);
		Iterator<Key> it=controlList.iterator();
		while(it.hasNext())
		{
			if(pm.getObjectById(Control.class, it.next()).equals(control))
				return false;
		}
		controlList.add(control.getControlID());
		return true;
	}
	public boolean addControl(List<Control> controlList) throws DeviceAndControlException
	{
		PersistenceManager pm = getPersistenceManager();
		boolean hasRepeat=false;
		boolean iRepeat;
		Iterator<Key> itc;
		Iterator<Control> itp=controlList.iterator();
		Control iControl;
		if(itp.hasNext())
		{
			while(itp.hasNext())
			{
				iControl=itp.next();
				itc=this.controlList.iterator();
				iRepeat=false;
				while(itc.hasNext())
				{
					if(pm.getObjectById(Control.class, itc.next()).equals(iControl))
					{
						hasRepeat=true;
						iRepeat=true;
						break;
					}
				}
				if(!iRepeat)
					this.controlList.add(iControl.getControlID());
			}
		}
		else
		{
			throw new DeviceAndControlException(DeviceAndControlException.ControlNotExist);
		}
		return hasRepeat;
	}
	public void clearControlList()
	{
		if(controlList!=null)
			controlList.clear();
	}
	/**
	 * @return the controlCollectionID
	 */
	public Key getControlCollectionID()
	{
		return controlCollectionID;
	}
	/**
	 * @return the userNickname
	 */
	public String getUserNickname()
	{
		return userNickname;
	}
	public List<Control> getControl()
	{
		PersistenceManager pm=getPersistenceManager();
		List<Control> re=new ArrayList<Control>();
		if(controlList!=null)
		{
			Iterator<Key> it=controlList.iterator();
			while(it.hasNext())
			{
				re.add(pm.getObjectById(Control.class, it.next()));
			}
		}
		return re;
	}
	/**
	 * @return the controlList
	 */
	public List<Key> getControlList()
	{
		return controlList;
	}
	/**
	 * @param controlCollectionID the controlCollectionID to set
	 */
	public void setControlCollectionID(Key controlCollectionID)
	{
		this.controlCollectionID = controlCollectionID;
	}
	/**
	 * @param userNickname the userNickname to set
	 */
	public void setUserNickname(String userNickname)
	{
		this.userNickname = userNickname;
	}
	/**
	 * @param controlList the controlList to set
	 */
	public void setControlList(List<Key> controlList)
	{
		this.controlList = controlList;
	}
}
