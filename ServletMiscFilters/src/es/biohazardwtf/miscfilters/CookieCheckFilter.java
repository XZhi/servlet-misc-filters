package es.biohazardwtf.miscfilters;

import java.io.IOException;
import java.util.ArrayList;
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

/**
 * This filter checks if the request contain the expected cookies. If
 * it detects any discrepancy the request may be rejected. It can be
 * configured to just log the problems found or take measures and
 * reject the request, redirecting the user to another page.
 */
public class CookieCheckFilter implements Filter {
	
	private List<String> expectedCookies;
	private FilterConfig filterConfigObj = null;
	
	private final int LENGTH = 90;

	public void init( FilterConfig config ) throws ServletException {
		
		this.filterConfigObj = config;
		String cookieParam = config.getInitParameter("expectedCookies");
		StringBuilder initText = new StringBuilder( FilterUtils.outputTextDelimiter(true, "*", LENGTH) );
		initText.append( FilterUtils.outputTextCentered("Servlet Misc Filters - Cookie Check Filter", "*", LENGTH) );
			
		if( cookieParam != null ){
			StringTokenizer token = new StringTokenizer(cookieParam, ",");
			this.expectedCookies = new ArrayList<String>();
		 
			while ( token.hasMoreTokens() ) {
				this.expectedCookies.add( token.nextToken() );
			}
		 
			for( String cookieFound : this.expectedCookies ){
				initText.append( FilterUtils.outputTextCentered("Expected cookie: " + cookieFound , "*", LENGTH) );
			}
			initText.append( FilterUtils.outputTextCentered("", "*", LENGTH) );
			initText.append( FilterUtils.outputTextCentered("Initialization successful!", "*", LENGTH) );
			 
		}else{
			initText.append( FilterUtils.outputTextCentered("Initialization failure! Parameter not found!", "*", LENGTH) );
			 
		}
		 
		initText.append( FilterUtils.outputTextDelimiter(false, "*", LENGTH) );
		this.filterConfigObj.getServletContext().log( initText.toString() );
		 
	}

	public void doFilter( ServletRequest req , ServletResponse res , FilterChain chain ) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) res;
		
		Cookie[] cookies = request.getCookies();	
		List<String> cookieList = new ArrayList<String>();
		for( Cookie c : cookies ){
			cookieList.add( c.getName() );
		}

		
		
		List<String> notFoundCookies = new ArrayList<String>();
		List<String> unexpectedFoundCookies = new ArrayList<String>();
		
		for ( String cookieName : expectedCookies ) {
			if ( cookieList.contains(cookieName) ){
				cookieList.remove(cookieName);
			
			}else{
				notFoundCookies.add(cookieName);
				
			}
		}
		
		for ( String cookieName : cookieList ){
			unexpectedFoundCookies.add(cookieName);
		}
		
		if( notFoundCookies.size() != 0 || unexpectedFoundCookies.size() != 0 ){
			
			StringBuilder errorText = new StringBuilder( FilterUtils.outputTextDelimiter(true, "*", LENGTH) );
			errorText.append( FilterUtils.outputTextCentered("Servlet Misc Filters - Cookie Check Filter", "*", LENGTH) );
			errorText.append( FilterUtils.outputTextCentered("Possible cookie manipulation found!", "*", LENGTH) );
			
			for( String cookieName : notFoundCookies ){
				errorText.append( FilterUtils.outputTextCentered("Cookie not found: " + cookieName, "*", LENGTH) );
			}
			
			for( String cookieName : unexpectedFoundCookies ){
				errorText.append( FilterUtils.outputTextCentered("Unexpected cookie found: " + cookieName, "*", LENGTH) );
			}
			
			errorText.append( FilterUtils.outputTextDelimiter(false, "*", LENGTH) );
			this.filterConfigObj.getServletContext().log( errorText.toString() );
		}
		
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
