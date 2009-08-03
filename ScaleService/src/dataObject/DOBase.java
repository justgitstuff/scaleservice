package dataObject;

import javax.jdo.PersistenceManager;

import factory.PMF;

public class DOBase
{
	private static PersistenceManager pm;
	public static PersistenceManager getPersistentManager()
	{
		if(pm==null)
			pm=PMF.get().getPersistenceManager();
		return pm;
	}
	public static void closePersistentManager()
	{
		if(pm!=null)
			pm.close();
		pm=null;
	}
}
