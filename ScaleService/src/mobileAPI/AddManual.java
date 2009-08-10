package mobileAPI;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Control;
import dataObject.ControlCollection;
import dataObject.Device;
import exception.DeviceAndControlException;
import exception.UserException;

public class AddManual extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 188910794411200828L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String deviceTag = req.getParameter("deviceTag");
			String command = req.getParameter("command");
			String parameter = req.getParameter("parameter");
			Device targetDevice=Device.getDevice(deviceTag);
			Control targetControl=targetDevice.getControl(command, parameter);
			ControlCollection cc=ControlCollection.getControlCollection();
			cc.addControl(targetControl);
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
			resp.sendRedirect("/mobile/success.html");
			ControlCollection.closePersistenceManager();
		}
	}
}
