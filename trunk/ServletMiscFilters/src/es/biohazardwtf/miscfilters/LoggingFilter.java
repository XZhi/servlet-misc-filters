package es.biohazardwtf.miscfilters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

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

public class LoggingFilter implements Filter {
	
	private boolean logRequestParams;
	private boolean logSessionParams;
	private boolean logCookies;
	private boolean logMiscInfo;
	private boolean initError;
	private final int LENGTH = 90;
	
	private FilterConfig filterConfigObj = null;

	public void init(FilterConfig config) throws ServletException {
		
		
		this.filterConfigObj = config;
		this.initError = false;
		
		StringBuilder initText = new StringBuilder( FilterUtils.outputTextDelimiter(true, "*", LENGTH) );
 		initText.append( FilterUtils.outputTextCentered("Servlet Misc Filters - Logging Filter", "*", LENGTH) );

		String param1 = config.getInitParameter("logRequestParams");
		String param2 = config.getInitParameter("logSessionParams");
		String param3 = config.getInitParameter("logCookies");
		String param4 = config.getInitParameter("logMiscInfo");
		
		if( param1 == null || param2 == null || param3 == null || param4 == null ){
    		this.initError = true;
    		initText.append( FilterUtils.outputTextCentered("Initialization failure! Parameters not found!", "*", LENGTH) );

		}else{    		
    		this.logRequestParams = Boolean.parseBoolean( param1 );
    		this.logSessionParams = Boolean.parseBoolean( param2 );
    		this.logCookies = Boolean.parseBoolean( param3 );
    		this.logMiscInfo = Boolean.parseBoolean( param4 );
    		
    		initText.append( FilterUtils.outputTextCentered("Log request parameters: " + this.logRequestParams, "*", LENGTH) );
    		initText.append( FilterUtils.outputTextCentered("Log session parameters: " + this.logSessionParams, "*", LENGTH) );
    		initText.append( FilterUtils.outputTextCentered("Log appended cookies: " + this.logCookies, "*", LENGTH) );
    		initText.append( FilterUtils.outputTextCentered("Log misc information: " + this.logMiscInfo, "*", LENGTH) );

    		initText.append( FilterUtils.outputTextCentered("", "*", LENGTH) );
    		initText.append( FilterUtils.outputTextCentered("Initialization successful!", "*", LENGTH) );
    		
    	}
		
		initText.append( FilterUtils.outputTextDelimiter(false, "*", LENGTH) );
		filterConfigObj.getServletContext().log( initText.toString() );
	
	}

	public void doFilter( ServletRequest req , ServletResponse res , FilterChain chain ) throws IOException, ServletException{

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		if( !initError ){
			
			StringBuilder textMessage = new StringBuilder( FilterUtils.outputTextDelimiter(true, "*", LENGTH) );
			textMessage.append( FilterUtils.outputTextCentered("Logging Filter - Request IP: " + request.getRemoteAddr(), "*", LENGTH) );

			if( this.logRequestParams ){
				textMessage.append( showRequestParameters(request) );
			}
			
			
			if( this.logSessionParams ){
				textMessage.append( showSessionAttributes(request) );
			}
			
			
			if( this.logCookies ){
				textMessage.append( showCookies(request) );
			}
			
			if( this.logMiscInfo ){
				textMessage.append( showRequestMiscInfo(request) );
			}

			textMessage.append( FilterUtils.outputTextDelimiter(false, "*", LENGTH) );
			filterConfigObj.getServletContext().log( textMessage.toString() );
			
		}else{
			
			StringBuilder errorText = new StringBuilder( FilterUtils.outputTextDelimiter(true, "-" , 90) );
    		errorText.append( FilterUtils.outputTextCentered("Logging Filter: Couldn't log message due to init errors!" , "|" , 90) );
			errorText.append( FilterUtils.outputTextDelimiter(false, "-",90) );
			
			filterConfigObj.getServletContext().log( errorText.toString() );

		}
	
		chain.doFilter(request, response);
		
	}

	

	public void destroy(){
	}
	
	
	private StringBuilder showRequestParameters( HttpServletRequest request ){
		
		Map<String, String[]> params = request.getParameterMap();
		Iterator<String> i = params.keySet().iterator();
		int count = 0;
		
		StringBuilder text = new StringBuilder( FilterUtils.outputTextDelimiter(false, "*", "-", LENGTH) );
		text.append( FilterUtils.outputTextCentered("Request parameters" , "*" , LENGTH) );
		
		if( !i.hasNext() ){
			text.append( FilterUtils.outputTextCentered("No parameters found!" , "*" , LENGTH) );
			return text;
		}
		
		//Look through Map data using Iterator
		while ( i.hasNext() ){
			String key = i.next();
			String value = params.get( key )[ 0 ];
			text.append( FilterUtils.outputTextCentered("Parameter [" + count + "]: " + key + "  Value: " + value , "*" , LENGTH) );
			count++;
		}
		
		return text;
	}


	private StringBuilder showSessionAttributes( HttpServletRequest request ){
		HttpSession session = request.getSession(false);

		StringBuilder text = new StringBuilder( FilterUtils.outputTextDelimiter(false, "*", "-", LENGTH) );
		text.append( FilterUtils.outputTextCentered("Session attributes" , "*" , LENGTH) );
		
		if( session == null ){
			text.append( FilterUtils.outputTextCentered("No session found!" , "*" , LENGTH) );
			return text;
		}

		ArrayList<String> list = Collections.list( session.getAttributeNames() );

		int count = 0;
		text.append( FilterUtils.outputTextCentered( "Session ID: " + session.getId() , "*" , LENGTH) );
		text.append( FilterUtils.outputTextCentered( "Session created on: " + new Date( session.getCreationTime() ) , "*" , LENGTH) );
		
		for( String param : list ){
			String value = session.getAttribute(param).toString();
			text.append( FilterUtils.outputTextCentered("Attribute [" + count + "]: " + param + "  Value: " + value , "*" , LENGTH) );
			count++;
		}
		
		return text;
	}
	

	private StringBuilder showCookies( HttpServletRequest request ){
		
		Cookie[] list = request.getCookies();
		
		StringBuilder text = new StringBuilder( FilterUtils.outputTextDelimiter(false, "*", "-", LENGTH) );
		text.append( FilterUtils.outputTextCentered("Request cookies" , "*" , LENGTH) );
		
		if( list == null ){
			text.append( FilterUtils.outputTextCentered("No cookies found!" , "*" , LENGTH) );
			return text;
		}
		
		int count = 0;
		
		for( Cookie c : list ){
			text.append( FilterUtils.outputTextCentered("Cookie [" + count + "]: " + c.getName() + "  Value: " + c.getValue(), "*" , LENGTH) );
			count++;
		}
		
		return text;
	}
	
	
	private StringBuilder showRequestMiscInfo( HttpServletRequest request ) {
		StringBuilder text = new StringBuilder( FilterUtils.outputTextDelimiter(false, "*", "-", LENGTH) );
		
		text.append( FilterUtils.outputTextCentered("Request misc information & Headers" , "*" , LENGTH) );		
		text.append( FilterUtils.outputTextCentered("Request Method: " + request.getMethod() , "*" , LENGTH) );
		text.append( FilterUtils.outputTextCentered("Is request secure?: " + request.isSecure() , "*" , LENGTH) );		
		text.append( FilterUtils.outputTextCentered("Request Query String: " + request.getQueryString() , "*" , LENGTH) );

		text.append( FilterUtils.outputTextCentered("" , "*" , LENGTH) );
		
		Enumeration<String> eNames = request.getHeaderNames();
		
		while ( eNames.hasMoreElements() ){
			String name = (String) eNames.nextElement();
			String value = request.getHeader(name);
			
			//Cookie values must be printed with their own method so we exclude that header field
			if( !name.equalsIgnoreCase("cookie") ){
				
				char[] chars = name.toLowerCase().toCharArray();
				chars[0] =  Character.toUpperCase( chars[0] );
				name = String.copyValueOf(chars);
				
				text.append( FilterUtils.outputTextCentered( name + ":  " + value , "*" , LENGTH) );
			}
			
		}
		
		return text;
	}

}
