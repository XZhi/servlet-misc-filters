package es.biohazardwtf.miscfilters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This method inspects the current HTTP request as well as the expected
 * cookie names method, and determines if there are any differences, either
 * more or fewer cookies.  If any difference is detected an error is thrown.
 *  
 * @see http://www.owasp.org/index.php/AppSensor_DetectionPoints#SE2:_Adding_New_Cookies
 * @see http://www.owasp.org/index.php/AppSensor_DetectionPoints#SE3:_Deleting_Existing_Cookies
 * 
 * @param expectedCookieNames the names of the expected cookies
 * 
 */
public class CookieCheckFilter implements Filter {
	
	private List<String> expectedCookies;
	
	private final int LENGTH = 90;

	public void init( FilterConfig config ) throws ServletException {
		
		 String cookieParam = config.getInitParameter("expectedCookies");
		 StringBuilder initText = new StringBuilder( FilterUtils.outputTextDelimiter(true, "*", LENGTH) );
	 		initText.append( FilterUtils.outputTextCentered("Servlet Misc Filters - Cookie Check Filter", "*", LENGTH) );
	 		
		 if( cookieParam != null ){
			 StringTokenizer token = new StringTokenizer(cookieParam, ",");
			 this.expectedCookies = new ArrayList<String>();
			 
			 while ( token.hasMoreTokens() ) {
				this.expectedCookies.add( token.nextToken() );
			 }

			// initText.append( FilterUtils.outputTextCentered("Error page URL: " + this.errorURL , "*", LENGTH) );
     		initText.append( FilterUtils.outputTextCentered("", "*", LENGTH) );
     		initText.append( FilterUtils.outputTextCentered("Initialization successful!", "*", LENGTH) );
			 
		 }else{
			 initText.append( FilterUtils.outputTextCentered("Initialization failure! Parameter not found!", "*", LENGTH) );
			 
		 }
		 
		 initText.append( FilterUtils.outputTextDelimiter(false, "*", LENGTH) );
		 config.getServletContext().log( initText.toString() );
		 
	}

	public void doFilter( ServletRequest req , ServletResponse res , FilterChain chain ) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) res;
    	HttpSession session = request.getSession(false);
		
		Cookie[] cookies = request.getCookies();
		//check for null set of cookies and handle by creating empty array - same result
		if (cookies == null) {
			cookies = new Cookie[0];
		}
		
		Collection<String> cookieList = new ArrayList<String>();
		
		//get names of actual cookies that are in the request
		for (Cookie cookie : cookies) {
			cookieList.add(cookie.getName());
		}
		
		//*************************************************
		//COMPROBAR SI LAS COOKIES ESTAN EN ORDEN Y CONTINUAR
		//*************************************************
		
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
