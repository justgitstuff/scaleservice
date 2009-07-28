/**
 * 
 */
package scaleService;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ÕıÕ¨÷€
 * 
 */
public class ViewSensor extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Sensor.class);
		query.setFilter("manufacturer == manu");
		query.declareParameters("String manu");
		try
		{
			List<Sensor> results = (List<Sensor>) query.execute("aa");
			if (results.iterator().hasNext())
			{
				for (Sensor e : results)
				{
					System.out.println(e.getSensorName());
				}
			}
		} finally
		{
			pm.close();
		}
	}
}
