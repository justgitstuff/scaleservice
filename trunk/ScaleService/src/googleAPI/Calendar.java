package googleAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Calendar extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8735623566733504951L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String urlStr = "http://www.google.com/calendar/feeds/default/private/full";  
		URL url = new URL(urlStr);  
		URLConnection URLconnection = url.openConnection();  
		HttpURLConnection httpConnection = (HttpURLConnection)URLconnection;  
		int responseCode = httpConnection.getResponseCode();  
		if (responseCode == HttpURLConnection.HTTP_OK)
		{ 
			InputStream urlStream = httpConnection.getInputStream();  
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));  
			String sCurrentLine = "";  
			String sTotalString = "";  
			while ((sCurrentLine = bufferedReader.readLine()) != null)
			{  
			    sTotalString += sCurrentLine;  
			}
			resp.getWriter().println(sTotalString);
		}
		else
		{
			resp.getWriter().println("Connection Error.");
		}  
	}
}
