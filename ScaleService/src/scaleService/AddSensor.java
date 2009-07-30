/**
 * 
 */
package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Sensor;
import exception.SensorException;
import factory.PMF;

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
		try
		{
			String sensorTag = req.getParameter("sensorTag");
			String sensorName = req.getParameter("sensorName");
			String location = req.getParameter("location");
			String manufacturer = req.getParameter("manufacturer");
			String description = req.getParameter("description");
			String memo = req.getParameter("memo");
			Sensor newSensor=new Sensor(sensorTag,sensorName,location,manufacturer, description, memo);
			newSensor.saveAsNew();
		} catch (SensorException e)
		{
			e.printStackTrace();
		} finally
		{
			PMF.saveAndClose();
			resp.sendRedirect("/index.jsp");
		}
	}
}
