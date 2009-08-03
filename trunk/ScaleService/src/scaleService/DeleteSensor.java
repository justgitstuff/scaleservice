package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Sensor;
import exception.SensorException;
import exception.UserException;

public class DeleteSensor extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3978387835017570195L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String sensorTag = req.getParameter("sensorTag");
			Sensor targetSensor=Sensor.getSensor(sensorTag);
			if(targetSensor==null)
				throw new SensorException(SensorException.SensorNotExist);
			Sensor.deleteDO(targetSensor);
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
