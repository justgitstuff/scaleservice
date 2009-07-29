package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.DataType;
import exception.DataTypeException;

public class AddSensorData extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8627048626074858946L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String typeName = req.getParameter("typeName");
		Double value = null;
		try
		{
			value = Double.parseDouble(req.getParameter("value"));
		}catch(NumberFormatException e)
		{
			e.printStackTrace();//Wrong Number Format
		}
		DataType targetDataType=DataType.getDataType(typeName);
		if(targetDataType==null)
		{
			try
			{
				throw new DataTypeException("Cannot find the Data type matches the typeName.");
			} catch (DataTypeException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			targetDataType.addSensorData(value);
			DataType.save();
			resp.sendRedirect("/index.jsp");
		}
	}
}
