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

import dataObject.Device;
import exception.DeviceAndControlException;
import exception.UserException;

public class EditDevice extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7746248564479586786L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String deviceTag = req.getParameter("deviceTag");
			String intro = req.getParameter("intro");
			Device targetDevice=Device.getDevice(deviceTag);
			if(targetDevice==null)
				throw new DeviceAndControlException(DeviceAndControlException.DeviceNotExist);
			targetDevice.setIntro(intro);
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
		} catch (DeviceAndControlException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			Device.closePersistenceManager();
		}
	}
}
