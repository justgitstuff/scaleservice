package dataObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import exception.SceneException;
import exception.UserException;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Scene extends DOBase
{
	@SuppressWarnings("unchecked")
	public static Scene getScene(String sceneName) throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
			PersistenceManager pm = getPersistenceManager();
			Query query = pm.newQuery(Scene.class);
			query.setFilter("userNickname == un");
			query.declareParameters("String un");
			List<Scene> results = (List<Scene>) query.execute(user.getNickname());
			Iterator<Scene> it=results.iterator();
			Scene iScene;
			while(it.hasNext())
			{
				iScene=it.next();
				if(iScene.getSceneName().equals(sceneName))
				{
					if(iScene.getControlID()==null)
						iScene.setControlID(new ArrayList<Key>());
					return iScene;
				}
			}
	    }
	    else
	    {
	    	throw new UserException(UserException.NotLogedIn);
	    }
	    return null;
	}
	@SuppressWarnings("unchecked")
	public static List<Scene> getSceneList() throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null)
	    {
			PersistenceManager pm = getPersistenceManager();
			Query query = pm.newQuery(Scene.class);
			query.setFilter("userNickname == un");
			query.declareParameters("String un");
			List<Scene> results = (List<Scene>) query.execute(user.getNickname());
			return results;
	    }
	    else
	    {
	    	throw new UserException(UserException.NotLogedIn);
	    }
	}
	@PrimaryKey
	@Persistent
	private Key sceneID;
	@Persistent
	private String userNickname;
	@Persistent
	private String sceneName;
	@Persistent
	private List<String> keyWord;
	@Persistent
	private List<Key> controlID;
	
	/**
	 * @param sceneID
	 * @param sceneName
	 * @param keyWord
	 * @param operationID
	 */
	public Scene(String sceneName)
	{
		super();
		this.sceneName = sceneName;
		this.keyWord = new ArrayList<String>();
		this.controlID = new ArrayList<Key>();
	}
	public void saveAsNew() throws SceneException, UserException
	{
		if(Scene.getScene(sceneName)==null && sceneID==null)
		{
			UserService userService = UserServiceFactory.getUserService();
		    User user = userService.getCurrentUser();
		    if (user != null)
		    {
		    	this.userNickname=user.getNickname();
		    	Key id = KeyFactory.createKey(Scene.class.getSimpleName(),userNickname+"."+sceneName);
		    	this.sceneID=id;
				PersistenceManager pm = getPersistenceManager();
				pm.makePersistent(this);
		    }
		    else
		    	throw new UserException(UserException.NotLogedIn);
		}
		else if(sceneID!=null)
		{
			throw new SceneException(SceneException.PrimaryKeyNotNull);
		}
		else
		{
			throw new SceneException(SceneException.SceneAlreadyExist);
		}
	}
	public void enterScene() throws UserException, DeviceAndControlException
	{
		ControlCollection cc=ControlCollection.getControlCollection();
		cc.addControl(getControl());
	}
	public void addControl(Key controlID)
	{
		this.controlID.add(controlID);
	}
	public void addControl(Control control) throws DeviceAndControlException
	{
		if(control.getControlID()==null)
			throw new DeviceAndControlException(DeviceAndControlException.ControlIsNew);
		this.controlID.add(control.getControlID());
	}
	public void addKeyWord(String keyWord)
	{
		this.keyWord.add(keyWord);
	}
	public void removeSceneControl(Control control)
	{
		List<Control> allControl=this.getControl();
		Iterator<Control> it=allControl.iterator();
		int index=0;
		while(it.hasNext())
		{
			if(it.next().equals(control))
			{
				controlID.remove(index);
				return;
			}
			index++;
		}
		return;
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
		PersistenceManager pm = getPersistenceManager();
		List<Control> controlList=new ArrayList<Control>();
		Iterator<Key> it=controlID.iterator();
		while(it.hasNext()) 
		{
			controlList.add(pm.getObjectById(Control.class, it.next()));
		}
		return controlList;
	}
	/**
	 * @return the sceneID
	 * @throws UserException 
	 */
	public Key getSceneID() throws UserException
	{
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		if(user!=null)
		{
			if(sceneID==null)
				this.sceneID = KeyFactory.createKey(Device.class.getSimpleName(),userNickname+"."+sceneName);
			return sceneID;
		}
		else
			throw new UserException(UserException.NotLogedIn);
	}
	/**
	 * @return the sceneName
	 */
	public String getSceneName()
	{
		return sceneName;
	}
	/**
	 * @return the keyWord
	 */
	public List<String> getKeyWord()
	{
		return keyWord;
	}
	/**
	 * @return the operationID
	 */
	public List<Key> getControlID()
	{
		return controlID;
	}
	/**
	 * @param sceneID the sceneID to set
	 */
	public void setSceneID(Key sceneID)
	{
		this.sceneID = sceneID;
	}
	/**
	 * @param sceneName the sceneName to set
	 */
	public void setSceneName(String sceneName)
	{
		this.sceneName = sceneName;
	}
	/**
	 * @param keyWord the keyWord to set
	 */
	public void setKeyWord(List<String> keyWord)
	{
		this.keyWord = keyWord;
	}
	/**
	 * @param controlID the operationID to set
	 */
	public void setControlID(List<Key> controlID)
	{
		this.controlID = controlID;
	}
	/**
	 * @param userNickname the userNickname to set
	 */
	public void setUserNickname(String userNickname)
	{
		this.userNickname = userNickname;
	}
	public boolean equals(Scene s)
	{
		if(sceneName.equals(s.getSceneName()))
			return true;
		else
			return false;
	}
}
