package scaleService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

import dataObject.Sensor;
import exception.SensorException;

public class ViewDataType extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9068971802042730116L;
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String sensorTag = req.getParameter("sensorTag");
			Sensor targetSensor=Sensor.getSensor(sensorTag);
			if(targetSensor==null)
				throw new SensorException("Sensor Not Exist");
			resp.setContentType("text/html;charset=gb2312");
			PrintWriter out=resp.getWriter();
			Element dataType=targetSensor.getDataTypeListXML();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(dataType); 
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);
		} catch (SensorException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			//do nothing
		}
	}
}
