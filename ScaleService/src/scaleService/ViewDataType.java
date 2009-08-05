package scaleService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
import dataObject.DataType;
import exception.DataTypeException;
import exception.UserException;

public class ViewDataType extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9068971802042730116L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		doGet(req,resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String typeName = req.getParameter("typeName");
			if(typeName==null)
			{
				resp.setContentType("text/html;charset=gb2312");
				PrintWriter out=resp.getWriter();
				Element dataType=Generator.buildDataTypeXML(DataType.getDataType());
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				DOMSource source = new DOMSource(dataType); 
				StreamResult result = new StreamResult(out);
				transformer.transform(source, result);
			}
			else
			{
				DataType targetDataType=DataType.getDataType(typeName);
				if(targetDataType==null)
					throw new DataTypeException(DataTypeException.DataTypeNotFonund);
				List<DataType> DataTypeInfo=new ArrayList<DataType>();
				DataTypeInfo.add(targetDataType);
				resp.setContentType("text/html;charset=gb2312");
				PrintWriter out=resp.getWriter();
				Element dataType=Generator.buildDataTypeXML(DataTypeInfo);
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				DOMSource source = new DOMSource(dataType); 
				StreamResult result = new StreamResult(out);
				transformer.transform(source, result);
			}
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
		} catch (DataTypeException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			//nothing
		}
	}
}
