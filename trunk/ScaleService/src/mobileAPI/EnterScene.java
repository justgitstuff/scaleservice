package mobileAPI;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Scene;
import exception.DeviceAndControlException;
import exception.UserException;

public class EnterScene extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2674076935021531629L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String sceneName = req.getParameter("sceneName");
		try
		{
			Scene targetScene=Scene.getScene(sceneName);
			targetScene.enterScene();
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
			Scene.closePersistenceManager();
		}
	}
}
