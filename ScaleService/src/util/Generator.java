/**
 * 
 */
package util;

import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataObject.Control;
import dataObject.DataType;
import dataObject.Device;
import dataObject.Operation;
import dataObject.Scene;
import dataObject.Sensor;
import dataObject.SensorData;

/**
 * @author ÕıÕ¨÷€
 *
 */
public class Generator
{
	public static Element buildOperationXML(List<Operation> OperationList)
	{
		DocumentBuilder db;
		try
		{
			db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		}
		Document XMLDoc=db.newDocument();
		Element root=XMLDoc.createElement("operation");
		try
		{
			Iterator<Operation> it=OperationList.iterator();
			if (it.hasNext())
			{
				for (Operation e : OperationList)
				{
					Element operationItem=XMLDoc.createElement("row");
					
					Element deviceTag=XMLDoc.createElement("deviceTag");
					deviceTag.appendChild(XMLDoc.createTextNode(e.getControl().getDevice().getDeviceTag()));
					
					Element controlCommand=XMLDoc.createElement("control_command");
					controlCommand.appendChild(XMLDoc.createTextNode(e.getControl().getCommand()));
					
					Element controlParam=XMLDoc.createElement("control_parameter");
					controlParam.appendChild(XMLDoc.createTextNode(e.getControl().getParameter()));
					
					Element dataTypeID=XMLDoc.createElement("dataTypeID");
					dataTypeID.appendChild(XMLDoc.createTextNode(e.getDataType().getTypeName()));
					
					Element direction=XMLDoc.createElement("direction");
					direction.appendChild(XMLDoc.createTextNode(e.getDirection().toString()));
					
					operationItem.appendChild(deviceTag);
					operationItem.appendChild(controlCommand);
					operationItem.appendChild(controlParam);
					operationItem.appendChild(dataTypeID);
					operationItem.appendChild(direction);
					root.appendChild(operationItem);
				}
			}
		} finally
		{
			//do nothing
		}
		return root;
	}
	public static Element buildDeviceXML(List<Device> deviceList)
	{
		DocumentBuilder db;
		try
		{
			db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		}
		Document XMLDoc=db.newDocument();
		Element root=XMLDoc.createElement("device");
		try
		{
			if (deviceList.iterator().hasNext())
			{
				for (Device e : deviceList)
				{
					Element deviceItem=XMLDoc.createElement("row");

					Element deviceTag=XMLDoc.createElement("deviceTag");
					deviceTag.appendChild(XMLDoc.createTextNode(e.getDeviceTag()));
					
					Element intro=XMLDoc.createElement("intro");
					intro.appendChild(XMLDoc.createTextNode(e.getIntro()));
					
					Element currentState=XMLDoc.createElement("currentState");
					currentState.appendChild(XMLDoc.createTextNode(e.getCurrentState()));
								
					deviceItem.appendChild(deviceTag);
					deviceItem.appendChild(intro);
					deviceItem.appendChild(currentState);
					
					root.appendChild(deviceItem);
				}
			}
		}
		finally
		{
			//do nothing
		}
		return root;
	}
	public static Element buildSenorDataXML(List<SensorData> sensorDataList)
	{
		DocumentBuilder db;
		try
		{
			db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		}
		Document XMLDoc=db.newDocument();
		Element root=XMLDoc.createElement("sensorData");
		try
		{
			Iterator<SensorData> it=sensorDataList.iterator();
			if (it.hasNext())
			{
				for (SensorData e : sensorDataList)
				{
					Element sensorDataItem=XMLDoc.createElement("row");

					Element value=XMLDoc.createElement("value");
					value.appendChild(XMLDoc.createTextNode(String.valueOf(e.getValue())));
					
					Element time=XMLDoc.createElement("time");
					time.appendChild(XMLDoc.createTextNode(e.getTime().toString()));
					
					sensorDataItem.appendChild(value);
					sensorDataItem.appendChild(time);
					root.appendChild(sensorDataItem);
				}
			}
		} finally
		{
			//do nothing
		}
		return root;
	}
	public static Element buildControlXML(List<Control> controlList)
	{
		DocumentBuilder db;
		try
		{
			db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		}
		Document XMLDoc=db.newDocument();
		Element root=XMLDoc.createElement("control");
		try
		{
			Iterator<Control> it=controlList.iterator();
			if (it.hasNext())
			{
				for (Control e : controlList)
				{
					Element controItem=XMLDoc.createElement("row");

					Element command=XMLDoc.createElement("command");
					command.appendChild(XMLDoc.createTextNode(e.getCommand()));
					
					Element parameter=XMLDoc.createElement("parameter");
					parameter.appendChild(XMLDoc.createTextNode(e.getParameter()));
					
					Element action=XMLDoc.createElement("action");
					action.appendChild(XMLDoc.createTextNode(e.getAction()));
										
					controItem.appendChild(command);
					controItem.appendChild(parameter);
					controItem.appendChild(action);
					
					root.appendChild(controItem);
				}
			}
		} finally
		{
			//do nothing
		}
		return root;
	}
	public static Element buildDataTypeXML(List<DataType> dataTypeList)
	{
		DocumentBuilder db;
		try
		{
			db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		}
		Document XMLDoc=db.newDocument();
		Element root=XMLDoc.createElement("dataType");
		try
		{
			Iterator<DataType> it=dataTypeList.iterator();
			if (it.hasNext())
			{
				for (DataType e : dataTypeList)
				{
					Element dataTypeItem=XMLDoc.createElement("row");

					Element unit=XMLDoc.createElement("unit");
					unit.appendChild(XMLDoc.createTextNode(e.getUnit()));
					
					Element typeName=XMLDoc.createElement("typeName");
					typeName.appendChild(XMLDoc.createTextNode(e.getTypeName()));
					
					Element maxCustom=XMLDoc.createElement("maxCustom");
					maxCustom.appendChild(XMLDoc.createTextNode(e.getMaxCustom().toString()));
					
					Element minCustom=XMLDoc.createElement("minCustom");
					minCustom.appendChild(XMLDoc.createTextNode(e.getMinCustom().toString()));
					
					dataTypeItem.appendChild(unit);
					dataTypeItem.appendChild(typeName);
					dataTypeItem.appendChild(maxCustom);
					dataTypeItem.appendChild(minCustom);
					root.appendChild(dataTypeItem);
				}
			}
		} finally
		{
			//do nothing
		}
		return root;
	}
	public static Element buildSensorXML(List<Sensor> sensorList)
	{
		DocumentBuilder db;
		try
		{
			db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		}
		Document XMLDoc=db.newDocument();
		Element root=XMLDoc.createElement("sensors");
		try
		{
			if (sensorList.iterator().hasNext())
			{
				for (Sensor e : sensorList)
				{
					Element sensorItem=XMLDoc.createElement("row");

					Element sensorTag=XMLDoc.createElement("sensorTag");
					sensorTag.appendChild(XMLDoc.createTextNode(e.getSensorTag()));
					
					Element sensorName=XMLDoc.createElement("sensorName");
					sensorName.appendChild(XMLDoc.createTextNode(e.getSensorName()));
					
					Element location=XMLDoc.createElement("location");
					location.appendChild(XMLDoc.createTextNode(e.getLocation()));
					
					Element manufacturer=XMLDoc.createElement("manufacturer");
					manufacturer.appendChild(XMLDoc.createTextNode(e.getManufacturer()));
					
					Element description=XMLDoc.createElement("description");
					description.appendChild(XMLDoc.createTextNode(e.getDescription()));
					
					Element memo=XMLDoc.createElement("memo");
					memo.appendChild(XMLDoc.createTextNode(e.getMemo()));
					
					sensorItem.appendChild(sensorTag);
					sensorItem.appendChild(sensorName);
					sensorItem.appendChild(location);
					sensorItem.appendChild(manufacturer);
					sensorItem.appendChild(description);
					sensorItem.appendChild(memo);
					root.appendChild(sensorItem);
				}
			}
		}
		finally
		{
			//do nothing
		}
		return root;
	}
	public static Element buildSceneXML(List<Scene> sceneList)
	{
		DocumentBuilder db;
		try
		{
			db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		}
		Document XMLDoc=db.newDocument();
		Element root=XMLDoc.createElement("scenes");
		try
		{
			if (sceneList.iterator().hasNext())
			{
				for (Scene e : sceneList)
				{
					Element sceneItem=XMLDoc.createElement("row");

					Element sceneName=XMLDoc.createElement("sceneName");
					sceneName.appendChild(XMLDoc.createTextNode(e.getSceneName()));
					
					Element keyWord=XMLDoc.createElement("keyWord");
					keyWord.appendChild(XMLDoc.createTextNode(e.getKeyWord().toString()));
					
					sceneItem.appendChild(sceneName);
					sceneItem.appendChild(keyWord);
					root.appendChild(sceneItem);
				}
			}
		}
		finally
		{
			//do nothing
		}
		return root;
	}
	
}
