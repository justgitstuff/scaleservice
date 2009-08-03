package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.DataType;
import exception.DataTypeException;
import exception.UserException;

public class EditDataType extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8991384141091094960L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String unit = req.getParameter("unit");
			String typeName = req.getParameter("typeName");
			Double maxCustom = Double.parseDouble(req.getParameter("maxCustom"));
			Double minCustom = Double.parseDouble(req.getParameter("minCustom"));
			DataType targetDataType=DataType.getDataType(typeName);
			if(targetDataType==null)
				throw new DataTypeException(DataTypeException.DataTypeNotFonund); 
			targetDataType.setAll(unit, typeName, maxCustom, minCustom);
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
