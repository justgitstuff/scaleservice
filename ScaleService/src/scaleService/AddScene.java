package scaleService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

import util.Generator;
import dataObject.Scene;
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
		try
		{
			Scene newScene=new Scene(sceneName);
			newScene.addKeyWord(firstKeyWord);
			newScene.saveAsNew();
			resp.setContentType("text/html;charset=gb2312");
			PrintWriter out=resp.getWriter();
			Element rc=Generator.buildActionReturn(Generator.SUCCESS);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(rc); 
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);
		} catch (SceneException e)
		{
			e.printStackTrace();
		} catch (UserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			Scene.closePersistenceManager();
		}
	}
}
