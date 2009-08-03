package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Sensor;
import exception.SensorException;
import exception.UserException;

public class EditSensor extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -469071743941946600L;
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
			Sensor targetSensor=Sensor.getSensor(sensorTag);
			if(targetSensor==null)
				throw new SensorException(SensorException.SensorNotExist);
			targetSensor.setSensorName(sensorName);
			targetSensor.setLocation(location);
			targetSensor.setManufacturer(manufacturer);
			targetSensor.setDescription(description);
			targetSensor.setMemo(memo);
			Sensor.closePersistenceManager();
		} catch (UserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SensorException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			Sensor.closePersistenceManager();
			resp.sendRedirect("/index.jsp");
		}
	}
}
