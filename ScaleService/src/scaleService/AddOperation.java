package scaleService;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Direction;
import dataObject.Control;
import dataObject.DataType;
import dataObject.Device;
import dataObject.Operation;
import exception.DataTypeException;
import exception.DeviceAndControlException;
import exception.OperationException;
import factory.PMF;

public class AddOperation extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3985639119689014897L;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String deviceTag = req.getParameter("deviceTag");
		String command = req.getParameter("command");
		String parameter = req.getParameter("parameter");
		String typeName = req.getParameter("typeName");
		String dir = req.getParameter("direction");
		try
		{
			Device targetDevice=Device.getDevice(deviceTag);
			if(targetDevice==null)
				throw new DeviceAndControlException(DeviceAndControlException.DeviceNotExist);
			Control control=targetDevice.getControl(command, parameter);
			if(control==null)
				throw new DeviceAndControlException(DeviceAndControlException.ControlNotExist);
			DataType targetDataType=DataType.getDataType(typeName);
			if(targetDataType==null)
				throw new DataTypeException(DataTypeException.TypeNameNotExist);
			Direction direction=dir.equalsIgnoreCase("up")?Direction.Up:Direction.Down;
			Operation newOperation=new Operation(control.getControlID(), targetDataType.getDataTypeID(), direction);
			newOperation.saveAsNew();
		} catch(DeviceAndControlException e)
		{
			e.printStackTrace();
		} catch (DataTypeException e)
		{
			e.printStackTrace();
		} catch (OperationException e)
		{
			e.printStackTrace();
		} finally
		{
			PMF.saveAndClose();
			resp.sendRedirect("/index.jsp");//Test Period Only
		}
	}
}
