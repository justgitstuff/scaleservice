/**
 * 
 */
package scaleService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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

import util.Generator;
import dataObject.Sensor;
import exception.SensorException;
import exception.UserException;

/**
 * @author ÕıÕ¨÷€
 * 
 */
public class ViewSensor extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -40117953954131465L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req,resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException
	{
		try
		{
			String sensorTag = req.getParameter("sensorTag");
			if(sensorTag==null)
			{
				resp.setContentType("text/html;charset=gb2312");
				PrintWriter out=resp.getWriter();
				Element sensors=Generator.buildSensorXML(Sensor.getSensor());
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				DOMSource source = new DOMSource(sensors); 
				StreamResult result = new StreamResult(out);
				transformer.transform(source, result);
			}
			else
			{
				Sensor targetSensor=Sensor.getSensor(sensorTag);
				if(targetSensor==null)
					throw new SensorException(SensorException.SensorNotExist);
				List<Sensor> sensorInfo=new ArrayList<Sensor>();
				sensorInfo.add(targetSensor);
				resp.setContentType("text/html;charset=gb2312");
				PrintWriter out=resp.getWriter();
				Element sensors=Generator.buildSensorXML(sensorInfo);
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				DOMSource source = new DOMSource(sensors); 
				StreamResult result = new StreamResult(out);
				transformer.transform(source, result);
			}
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
			//nothing
		}
	}
}
