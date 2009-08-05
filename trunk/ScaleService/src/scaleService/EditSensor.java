package scaleService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

import util.Generator;

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
			resp.setContentType("text/html;charset=gb2312");
			PrintWriter out=resp.getWriter();
			Element rc=Generator.buildActionReturn(Generator.SUCCESS);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(rc); 
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);
		} catch (UserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SensorException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e)
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
