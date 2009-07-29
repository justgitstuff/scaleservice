package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Device;
import exception.DeviceAndControlException;
import factory.ControlFactory;

public class AddControl extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4883533029732177783L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String deviceTag = req.getParameter("deviceTag");
		String command = req.getParameter("command");
		String parameter = req.getParameter("parameter");
		String action = req.getParameter("action");
		Device targetDevice=Device.getDevice(deviceTag);
		if(targetDevice==null)
		{
			try
			{
				throw new DeviceAndControlException("Cannot find the device matches the device tag.");
			} catch (DeviceAndControlException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			targetDevice.addControl(ControlFactory.get().newControl(command, parameter, action));
			Device.save();
			resp.sendRedirect("/index.jsp");
		}
	}
}
