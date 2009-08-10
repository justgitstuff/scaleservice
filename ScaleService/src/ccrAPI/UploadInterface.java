package ccrAPI;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.DataType;
import dataObject.Device;
import dataObject.Sensor;
import exception.DeviceAndControlException;
import exception.SensorException;
import factory.ControlFactory;

public class UploadInterface extends HttpServlet
{
	/**
	 * 
	 */
	private String userNickname;
	private static final long serialVersionUID = 8682308471366148985L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String comString = req.getParameter("comString");
		userNickname = req.getParameter("username");
		String sensorTag,sensorName,manufacturer,description,value,unit,deviceTag,intro,command,parameter;
		switch(comString.charAt(0))
		{
			case '!'://Register Sensor
				sensorTag=comString.substring(1, 21).trim();
				sensorName=comString.substring(21, 36).trim();
				manufacturer=comString.substring(36, 46).trim();
				description=comString.substring(46).trim();
				if(addSensor(sensorTag, sensorName, manufacturer, description))
					resp.getWriter().println('1');
				else
					resp.getWriter().println('0');
				break;
			case '@'://add SensorData
				sensorTag = comString.substring(1,21).trim();
				value = comString.substring(21,29).trim();
				unit = comString.substring(29,39).trim();
				if(addSensorData(sensorTag, unit, value))
					resp.getWriter().println('1');
				else
					resp.getWriter().println('0');
				break;
        	case '$'://add Device
        		deviceTag = comString.substring(1,21).trim();
        		intro = comString.substring(21,41).trim();
        		if(regDevice(deviceTag, intro))
        			resp.getWriter().println('1');
				else
					resp.getWriter().println('0');
        		break;
        	case '?'://add Command
        		deviceTag = comString.substring(1,21).trim();
        		command = comString.substring(21,31).trim();
        		parameter = comString.substring(31,36).trim();
        		if(regCommand(deviceTag, command, parameter))
        			resp.getWriter().println('1');
				else
					resp.getWriter().println('0');
        		break;
        	default:
        		resp.getWriter().println('0');
		}
	}
	public boolean addSensorData(String sensorTag,String unit,String value)
	{
		try
		{
			Double dvalue = Double.parseDouble(value);
			Sensor targetSensor=Sensor.getSensor(sensorTag,userNickname);
			if(targetSensor==null)
				throw new SensorException(SensorException.SensorNotExist);
			targetSensor.addSensorData(dvalue, unit,userNickname);
			return true;
		} catch(Exception e)
		{
			return false;
		} finally
		{
			DataType.closePersistenceManager();
		}
	}
	public boolean addSensor(String sensorTag,String sensorName,String manufacturer,String description)
	{
		try
		{
			Sensor targetSensor=Sensor.getSensor(sensorTag, userNickname);
			if(targetSensor==null)
			{
				String location = "Unknown";
				String memo = "Unknown";
				Sensor newSensor=new Sensor(sensorTag,sensorName,location,manufacturer, description, memo);
				newSensor.saveToUser(userNickname);
			}
			return true;
		} catch (Exception e)
		{
			return false;
		} finally
		{
			Sensor.closePersistenceManager();
		}
	}
	public boolean regDevice(String deviceTag,String intro)
	{
		try
		{
			Device newDevice=new Device(deviceTag,intro);
			newDevice.saveToUser(userNickname);
			return true;
		} catch (Exception e)
		{
			return false;
		} finally
		{
			Device.closePersistenceManager();
		}
	}
	public boolean regCommand(String deviceTag,String command,String parameter)
	{
		try
		{
			String action = "Unknown";
			Device targetDevice=Device.getDevice(deviceTag,userNickname);
			if(targetDevice==null)
				throw new DeviceAndControlException(DeviceAndControlException.DeviceNotExist);
			targetDevice.addControl(ControlFactory.get().newControl(command, parameter, action));
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		} finally
		{
			Device.closePersistenceManager();
		}
	}
}
