/**
 * 
 */
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
			resp.setContentType("text/html;charset=gb2312");
			PrintWriter out=resp.getWriter();
			Element rc=Generator.buildActionReturn(Generator.SUCCESS);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(rc); 
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);
		} catch (SensorException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserException e)
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
		}
	}
}
