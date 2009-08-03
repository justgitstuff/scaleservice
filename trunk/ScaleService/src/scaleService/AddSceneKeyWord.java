package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Scene;
import exception.SceneException;
import exception.UserException;

public class AddSceneKeyWord extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 600381517345734342L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String sceneName = req.getParameter("sceneName");
		String keyWord = req.getParameter("keyWord");
		try
		{
			Scene targetScene=Scene.getScene(sceneName);
			if(targetScene==null)
				throw new SceneException(SceneException.SceneNotExist);
			targetScene.addKeyWord(keyWord);
			Scene.closePersistentManager();
		} catch (SceneException e)
		{
			e.printStackTrace();
		} catch (UserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			resp.sendRedirect("/index.jsp");//Test Period Only
		}
	}
}
