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

import dataObject.Operation;
import factory.PMF;

public class ViewToDo extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -939617794703400960L;
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException
	{
		try
		{
			resp.setContentType("text/html;charset=gb2312");
			PrintWriter out=resp.getWriter();
			Element operation=Generator.buildOperationXML(Operation.findAllToDo());
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(operation); 
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
		} finally
		{
			PMF.saveAndClose();
		}
	}
}
