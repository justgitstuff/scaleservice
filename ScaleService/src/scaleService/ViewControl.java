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

import dataObject.Device;
import exception.DeviceAndControlException;

public class ViewControl extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4158153639004837801L;
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String deviceTag = req.getParameter("deviceTag");
		Device targetDevice=Device.getDevice(deviceTag);
		if(targetDevice==null)
		{
			DeviceAndControlException e = new DeviceAndControlException("Cannot find the Device matches the deviceTag.");
			e.printStackTrace();
		}
		else
		{
			resp.setContentType("text/html;charset=gb2312");
			PrintWriter out=resp.getWriter();
			Element control=targetDevice.getControlListXML();
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
			DOMSource source = new DOMSource(control); 
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
}
