package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Device;
import exception.DeviceAndControlException;
import exception.UserException;

public class DeleteDevice extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3079539654034671757L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String deviceTag = req.getParameter("deviceTag");
			Device targetDevice=Device.getDevice(deviceTag);
			if(targetDevice==null)
				throw new DeviceAndControlException(DeviceAndControlException.DeviceNotExist);
			Device.deleteDO(targetDevice);
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
			Device.closePersistenceManager();
			resp.sendRedirect("/index.jsp");
		}
	}
}
