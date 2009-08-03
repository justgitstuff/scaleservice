package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.DataType;
import exception.DataTypeException;
import exception.UserException;

public class DeleteDataType extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6288290312743628849L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		try
		{
			String typeName = req.getParameter("typeName");
			DataType targetDataType=DataType.getDataType(typeName);
			if(targetDataType==null)
				throw new DataTypeException(DataTypeException.DataTypeNotFonund); 
			DataType.deleteDO(targetDataType);
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
			DataType.closePersistenceManager();
			resp.sendRedirect("/index.jsp");
		}
	}
}
