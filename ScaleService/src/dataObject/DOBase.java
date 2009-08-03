package dataObject;

import javax.jdo.PersistenceManager;

import factory.PMF;

public class DOBase
{
	private static PersistenceManager pm;
	public static PersistenceManager getPersistenceManager()
	{
		if(pm==null)
			pm=PMF.get().getPersistenceManager();
		return pm;
	}
	public static void deleteDO(Object DO)
	{
		PersistenceManager pm=getPersistenceManager();
		pm.deletePersistent(DO);
	}
	public static void closePersistenceManager()
	{
		if(pm!=null)
			pm.close();
		pm=null;
	}
}
