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
import dataObject.Scene;
import exception.SceneException;
import exception.UserException;

public class ViewSceneControl extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2676632169893075242L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req,resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String sceneName = req.getParameter("sceneName");
			Scene targetScene=Scene.getScene(sceneName);
			if(targetScene==null)
				throw new SceneException(SceneException.SceneNotExist);
			resp.setContentType("text/html;charset=gb2312");
			PrintWriter out=resp.getWriter();
			Element control=Generator.buildControlXML(targetScene.getControl());
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(control); 
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);
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
		} catch (UserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SceneException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			//nothing
		}
	}
}
