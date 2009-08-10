package ccrAPI;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Pong extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4230305894892430041L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String returnString="Command:"+req.getParameter("comString")+"Username:"+req.getParameter("username")+"Password:"+req.getParameter("password");
		resp.getWriter().println(returnString);	
	}
}
