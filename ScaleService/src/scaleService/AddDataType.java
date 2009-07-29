package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.DataType;
import dataObject.Sensor;
import exception.SensorException;

public class AddDataType extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -198578773814512863L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String unit = req.getParameter("unit");
		String typeName = req.getParameter("typeName");
		String maxCustom = req.getParameter("maxCustom");
		String minCustom = req.getParameter("minCustom");
		String sensorTag = req.getParameter("sensorTag");
		DataType newDataType=new DataType(unit,typeName,maxCustom,minCustom);
		Sensor targetSensor=Sensor.getSensor(sensorTag);
		if(targetSensor==null)
		{
			try
			{
				throw new SensorException("Cannot find the Sensor matches the sensorTag.");
			} catch (SensorException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			targetSensor.addDataType(newDataType);
			Sensor.save();
			resp.sendRedirect("/index.jsp");
		}
	}
}
