/**
 * 
 */
package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Sensor;

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
		String sensorTag = req.getParameter("sensorTag");
		String sensorName = req.getParameter("sensorName");
		String location = req.getParameter("location");
		String manufacturer = req.getParameter("manufacturer");
		Sensor newSensor=new Sensor(sensorTag,sensorName,location,manufacturer);
		newSensor.save();
		resp.sendRedirect("/addSensor.jsp");
	}
}
