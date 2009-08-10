package ccrAPI;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Control;
import dataObject.ControlCollection;
import dataObject.Operation;

public class DownloadInterface extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4361589312731383739L;
	private String userNickname;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String returnString="";
		userNickname = req.getParameter("username");
		ControlCollection cc=ControlCollection.getControlCollection(userNickname);
		List<Control> ret=Operation.findAllControl(userNickname);
		if(cc!=null)
		{
			ret.addAll(cc.getControl());
			cc.clearControlList();
		}	
		Iterator<Control> it=ret.iterator();
		while(it.hasNext())
		{
			Control returnControl=it.next();
			returnString+="^"+returnControl.getDevice().getDeviceTag()+returnControl.getCommand()+returnControl.getParameter()+"*";
		}
		resp.getWriter().println(returnString);
		ControlCollection.closePersistenceManager();	
	}
}
