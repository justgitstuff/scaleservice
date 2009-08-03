package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Device;
import dataObject.Scene;
import exception.SceneException;
import exception.UserException;

public class DeleteScene extends HttpServlet
{
	private static final long serialVersionUID = -3079539654034671757L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String sceneName = req.getParameter("sceneName");
			Scene targetScene=Scene.getScene(sceneName);
			if(targetScene==null)
				throw new SceneException(SceneException.SceneNotExist);
			Scene.deleteDO(targetScene);
		} catch (UserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SceneException e)
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
