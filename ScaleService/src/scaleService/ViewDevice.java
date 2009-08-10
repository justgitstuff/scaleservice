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
import dataObject.Device;
import exception.DeviceAndControlException;
import exception.UserException;

public class ViewDevice extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4728912376782665126L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req,resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws IOException
	{
		try
		{
			String deviceTag = req.getParameter("deviceTag");
			if(deviceTag==null)
			{
				resp.setContentType("text/html;charset=gb2312");
				PrintWriter out=resp.getWriter();
				Element devices=Generator.buildDeviceXML(Device.getDeviceList());
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				DOMSource source = new DOMSource(devices); 
				StreamResult result = new StreamResult(out);
				transformer.transform(source, result);
			}
			else
			{
				Device targetDevice=Device.getDevice(deviceTag);
				if(targetDevice==null)
					throw new DeviceAndControlException(DeviceAndControlException.DeviceNotExist);
				List<Device> DeviceInfo=new ArrayList<Device>();
				DeviceInfo.add(targetDevice);
				resp.setContentType("text/html;charset=gb2312");
				PrintWriter out=resp.getWriter();
				Element devices=Generator.buildDeviceXML(DeviceInfo);
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				DOMSource source = new DOMSource(devices); 
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
		} catch (DeviceAndControlException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			//nothing
		}
	}
}
