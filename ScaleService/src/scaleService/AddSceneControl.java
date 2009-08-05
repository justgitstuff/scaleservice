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

import util.Generator;
import dataObject.Control;
import dataObject.Device;
import dataObject.Scene;
import exception.DeviceAndControlException;
import exception.SceneException;
import exception.UserException;

public class AddSceneControl extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8053509545125831546L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String sceneName = req.getParameter("sceneName");
		String deviceTag = req.getParameter("deviceTag");
		String command = req.getParameter("command");
		String parameter = req.getParameter("parameter");
		try
		{
			Device targetDevice=Device.getDevice(deviceTag);
			if(targetDevice==null)
				throw new DeviceAndControlException(DeviceAndControlException.DeviceNotExist);
			Control control=targetDevice.getControl(command, parameter);
			if(control==null)
				throw new DeviceAndControlException(DeviceAndControlException.ControlNotExist);
			Scene targetScene=Scene.getScene(sceneName);
			if(targetScene==null)
				throw new SceneException(SceneException.SceneNotExist);
			targetScene.addControl(control);
			resp.setContentType("text/html;charset=gb2312");
			PrintWriter out=resp.getWriter();
			Element rc=Generator.buildActionReturn(Generator.SUCCESS);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(rc); 
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);
		} catch(DeviceAndControlException e)
		{
			e.printStackTrace();
		} catch (SceneException e)
		{
			e.printStackTrace();
		} catch (UserException e)
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
			Scene.closePersistenceManager();
		}
	}
}
