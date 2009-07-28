/**
 * 
 */
package factory;

import java.util.Date;

import dataObject.SensorData;

/**
 * @author ÕıÕ¨÷€
 *
 */
public class SensorDataFactory
{
	private static SensorDataFactory unique;
	/**
	 * 
	 */
	private SensorDataFactory()
	{
		// TODO Auto-generated constructor stub
	}
	public static SensorDataFactory get()
	{
		if(unique==null)
			unique=new SensorDataFactory();
		return unique;
	}
	public SensorData newSensorData(double value)
	{
		SensorData r=new SensorData(value,new Date());
		return r;
	}
}
