package es.biohazardwtf.miscfilters;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;


public class NoCacheFilter implements Filter {
	private FilterConfig filterConfigObj;
	 
	public void init(FilterConfig config) throws ServletException{
		this.filterConfigObj = config;
		
		StringBuilder initText = new StringBuilder( FilterUtils.outputTextDelimiter(true, "*" ) );
		initText.append( FilterUtils.outputTextCentered("Servlet Misc Filters - No Cache Filter", "*" ) );
		initText.append( FilterUtils.outputTextCentered("Initialization successful!", "*" ) );
		initText.append( FilterUtils.outputTextDelimiter(false, "*" ) );
		this.filterConfigObj.getServletContext().log( initText.toString() );
	}
	
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain )throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) res;
		
		/*Set the appropriate headers*/
		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		httpResponse.setDateHeader("Expires", 0);
		httpResponse.setHeader("Pragma", "No-cache");
		
		chain.doFilter(req, res);
	}
	
	public void destroy() {
	}
}