package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		} finally
		{
			DataType.closePersistenceManager();
			resp.sendRedirect("/index.jsp");
		}
	}
}
