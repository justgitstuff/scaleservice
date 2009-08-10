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
import dataObject.Control;
import dataObject.DataType;
import dataObject.Device;
import dataObject.Operation;
import exception.DataTypeException;
import exception.DeviceAndControlException;
import exception.UserException;

public class DeleteOperation extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -281728134003605340L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String deviceTag = req.getParameter("deviceTag");
		String command = req.getParameter("command");
		String parameter = req.getParameter("parameter");
		String typeName = req.getParameter("typeName");
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
			Operation targetOperation=Operation.getOperation(control.getControlID(), targetDataType.getDataTypeID());
			targetOperation.deleteThis();
			resp.setContentType("text/html;charset=gb2312");
			PrintWriter out=resp.getWriter();
			Element rc=Generator.buildActionReturn(Generator.SUCCESS);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(rc); 
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);
		} catch(DeviceAndControlException e)
		{
			e.printStackTrace();
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
			Operation.closePersistenceManager();
		}
	}
}
