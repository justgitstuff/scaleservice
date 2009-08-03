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
import exception.OperationException;
import exception.UserException;
import factory.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ControlCollection
{
	@SuppressWarnings("unchecked")
	public ControlCollection getControlCollection()
	{
		ControlCollection returnControlCollection=null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ControlCollection.class);
		try
		{
			List<ControlCollection> results = (List<ControlCollection>) query.execute();
			if (results.iterator().hasNext())
			{
				returnControlCollection = results.iterator().next();
			}
		}finally
		{
			//TODO ControlCollection之类的用户机制
		}
		return returnControlCollection;
	}
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key controlCollectionID;
	@Persistent
	private List<Control> controlList;
	public ControlCollection()
	{
		//Init
	}
	/**
	 * 将Control插入待执行列表中
	 * @param control
	 * @return
	 * @throws DeviceAndControlException 
	 */
	public boolean addControl(Control control) throws DeviceAndControlException
	{
		if(control==null)
			throw new DeviceAndControlException(DeviceAndControlException.ControlNotExist);
		Iterator<Control> it=controlList.iterator();
		while(it.hasNext())
		{
			if(it.next().equals(control))
				return false;
		}
		controlList.add(control);
		return true;
	}
	public boolean addControl(List<Control> controlList) throws DeviceAndControlException
	{
		boolean hasRepeat=false;
		boolean iRepeat;
		Iterator<Control> itc;
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
					if(itc.next().equals(iControl))
					{
						hasRepeat=false;
						iRepeat=true;
						break;
					}
				}
				if(!iRepeat)
					this.controlList.add(iControl);
			}
		}
		else
		{
			throw new DeviceAndControlException(DeviceAndControlException.ControlNotExist);
		}
		return hasRepeat;
	}
	public boolean addOperation(Operation operation) throws OperationException, DeviceAndControlException
	{
		if(operation==null)
			throw new OperationException(OperationException.OperationNotExist);
		else
		{
			Control control=operation.getControl();
			return addControl(control);
		}
	}
	public boolean addOperation(List<Operation> operation) throws OperationException, DeviceAndControlException
	{
		List<Control> controlList=new ArrayList<Control>();
		Iterator<Operation> it=operation.iterator();
		if(it.hasNext())
		{
			while(it.hasNext())
			{
				controlList.add(it.next().getControl());
				return addControl(controlList);
			}
		}
		else
			throw new OperationException(OperationException.OperationNotExist);
		return false;
	}
	/**
	 * @return the controlCollectionID
	 */
	public Key getControlCollectionID()
	{
		return controlCollectionID;
	}
	/**
	 * 获取所有的操作，然后清空操作列表
	 * @return the controlList
	 */
	public List<Control> getControlList()
	{
		return controlList;
	}
	/**
	 * 获取所有操作，并加入自动操作，返回后清空操作列表
	 * @return
	 * @throws OperationException 
	 * @throws DeviceAndControlException 
	 * @throws UserException 
	 */
	public List<Control> getControlListWithOperation() throws OperationException, DeviceAndControlException, UserException
	{
		this.addOperation(Operation.findAllToDo());
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
	 * @param controlList the controlList to set
	 */
	public void setControlList(List<Control> controlList)
	{
		this.controlList = controlList;
	}
}
