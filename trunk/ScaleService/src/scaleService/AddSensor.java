/**
 * 
 */
package scaleService;

import java.io.IOException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ÕıÕ¨÷€
 * 
 */
public class AddSensor extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7728184558568191804L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String sensorName = req.getParameter("sensorName");
		String location = req.getParameter("location");
		String manufacturer = req.getParameter("manufacturer");
		Sensor newSensor=new Sensor(sensorName,location,manufacturer);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			pm.makePersistent(newSensor);
		} finally
		{
			pm.close();
		}
		resp.sendRedirect("/addSensor.jsp");
	}
}
