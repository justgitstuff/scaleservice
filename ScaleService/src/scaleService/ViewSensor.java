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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

import dataObject.Sensor;

/**
 * @author ÕıÕ¨÷€
 * 
 */
public class ViewSensor extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException
	{
		resp.setContentType("text/html;charset=gb2312");
		PrintWriter out=resp.getWriter();
		Element sensors=Sensor.getSensorXML();
		Transformer transformer;
		try
		{
			transformer = TransformerFactory.newInstance().newTransformer();
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
			return;
		}
		DOMSource source = new DOMSource(sensors); 
		StreamResult result = new StreamResult(out);
		try
		{
			transformer.transform(source, result);
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
			return;
		}
	}
}
