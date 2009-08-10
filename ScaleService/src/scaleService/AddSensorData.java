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

import dataObject.DataType;
import exception.DataTypeException;
import exception.UserException;
import factory.SensorDataFactory;

public class AddSensorData extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8627048626074858946L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String typeName = req.getParameter("typeName");
			Double value = null;
			value = Double.parseDouble(req.getParameter("value"));
			DataType targetDataType=DataType.getDataType(typeName);
			if(targetDataType==null)
				throw new DataTypeException("Cannot find the Data type matches the typeName.");
			targetDataType.addSensorData(SensorDataFactory.get().newSensorData(value));
			resp.setContentType("text/html;charset=gb2312");
			PrintWriter out=resp.getWriter();
			Element rc=Generator.buildActionReturn(Generator.SUCCESS);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(rc); 
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);
		} catch(NumberFormatException e)
		{
			e.printStackTrace();//Wrong Number Format
		} catch (DataTypeException e)
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
			DataType.closePersistenceManager();
		}
	}
}
