package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Device;
import exception.DeviceAndControlException;
import factory.ControlFactory;
import factory.PMF;

public class AddControl extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4883533029732177783L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String deviceTag = req.getParameter("deviceTag");
			String command = req.getParameter("command");
			String parameter = req.getParameter("parameter");
			String action = req.getParameter("action");
			Device targetDevice=Device.getDevice(deviceTag);
			if(targetDevice==null)
				throw new DeviceAndControlException(DeviceAndControlException.DeviceNotExist);
			targetDevice.addControl(ControlFactory.get().newControl(command, parameter, action));
		} catch(DeviceAndControlException e)
		{
			e.printStackTrace();
		} finally
		{
			PMF.saveAndClose();
			resp.sendRedirect("/index.jsp");
		}
	}
}
