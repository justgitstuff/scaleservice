package factory;

import dataObject.Control;

public class ControlFactory
{
	private static ControlFactory unique;
	private ControlFactory()
	{
		
	}
	public static ControlFactory get()
	{
		if(unique==null)
			unique=new ControlFactory();
		return unique;
	}
	public Control newControl(String command,String parameter,String action)
	{
		Control returnControl=new Control(command, parameter, action);
		return returnControl;
	}
}
