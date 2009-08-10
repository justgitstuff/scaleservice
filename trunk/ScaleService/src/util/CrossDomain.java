package util;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrossDomain extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5526810371462028984L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String policyFile = "<?xml version='1.0' encoding='utf-8'?>";
			
			policyFile += "<cross-domain-policy>";
				policyFile += "<site-control permitted-cross-domain-policies='all' />";
				policyFile += "<allow-access-from domain='*' />";
			policyFile += "</cross-domain-policy>";
			resp.addHeader("Content-Type","text/xml; charset=UTF-8");
			resp.addHeader("X-Permitted-Cross-Domain-Policies", "all");
			resp.getWriter().println(policyFile);
	}
}
