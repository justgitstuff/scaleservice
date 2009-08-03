package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Control;
import dataObject.Device;
import dataObject.Scene;
import exception.DeviceAndControlException;
import exception.SceneException;
import exception.UserException;

public class AddScene extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 29822517966608476L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String sceneName = req.getParameter("sceneName");
		String firstKeyWord = req.getParameter("firstKeyWord");
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
			Scene newScene=new Scene(sceneName);
			newScene.addKeyWord(firstKeyWord);
			newScene.addControl(control);
			newScene.saveAsNew();
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
		} finally
		{
			Scene.closePersistenceManager();
			resp.sendRedirect("/index.jsp");//Test Period Only
		}
	}
}
