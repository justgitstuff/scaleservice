package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.DataType;
import dataObject.Sensor;
import exception.SensorException;
import factory.PMF;

public class AddDataType extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -198578773814512863L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String unit = req.getParameter("unit");
			String typeName = req.getParameter("typeName");
			String maxCustom = req.getParameter("maxCustom");
			String minCustom = req.getParameter("minCustom");
			String sensorTag = req.getParameter("sensorTag");
			DataType newDataType=new DataType(unit,typeName,maxCustom,minCustom);
			Sensor targetSensor=Sensor.getSensor(sensorTag);
			if(targetSensor==null)
				throw new SensorException(SensorException.SensorNotExist);
			targetSensor.addDataType(newDataType);
		} catch(SensorException e)
		{
			e.printStackTrace();
		} finally
		{
			PMF.saveAndClose();
			resp.sendRedirect("/index.jsp");
		}
	}
}
