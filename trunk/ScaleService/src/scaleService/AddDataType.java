package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.DataType;
import exception.DataTypeException;
import exception.UserException;

public class AddDataType extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -198578773814512863L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String unit = req.getParameter("unit");
			String typeName = req.getParameter("typeName");
			Double maxCustom = Double.parseDouble(req.getParameter("maxCustom"));
			Double minCustom = Double.parseDouble(req.getParameter("minCustom"));
			DataType newDataType=DataType.getDataType(typeName);
			if(newDataType!=null)
				throw new DataTypeException(DataTypeException.TypeNameAlreadyExist); 
			newDataType=new DataType(unit,typeName,maxCustom,minCustom);
			newDataType.saveAsNew();
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
			DataType.closePersistentManager();
			resp.sendRedirect("/index.jsp");
		}
	}
}
