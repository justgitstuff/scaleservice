package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Device;
import exception.DeviceAndControlException;
import factory.PMF;

public class AddDevice extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1639979083766876774L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String deviceTag = req.getParameter("deviceTag");
			String intro = req.getParameter("intro");
			Device newDevice=new Device(deviceTag,intro);
			newDevice.saveAsNew();
		} catch (DeviceAndControlException e)
		{
			e.printStackTrace();
		} finally
		{
			PMF.saveAndClose();
			resp.sendRedirect("/index.jsp");
		}
	}
}
