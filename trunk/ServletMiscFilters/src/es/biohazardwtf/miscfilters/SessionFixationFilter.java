package es.biohazardwtf.miscfilters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//Tomcat 7 utiliza un filtro interno de fijacion de sesion, aunque este es mas completo. 
//El de tomcat 7 requiere usar el authenticate, asi que este filtro tambien puede ser algo mas versatil.
public class SessionFixationFilter implements Filter {
	
	private FilterConfig filterConfigObj = null;
	private int sessionRenewalThreshold;
	private boolean initError;
	private String errorURL;
	private boolean allowURLRewriting;
	private boolean strictControl;
	
	private final int LENGTH = 90;
	
    public void init(FilterConfig config) throws ServletException {    	
    	
    	this.filterConfigObj = config;
       
    	StringBuilder initText = new StringBuilder( FilterUtils.outputTextDelimiter(true, "*" ) );
 		initText.append( FilterUtils.outputTextCentered("Servlet Misc Filters - Session Fixation Protection Filter", "*" ) );
        
        String param1 = config.getInitParameter("sessionRenewalThreshold");
        String param2 = config.getInitParameter("allowURLRewriting");
        String param3 = config.getInitParameter("strictControl");
        this.errorURL = config.getInitParameter("errorURL");
        
        if( param1==null || param2==null || param3==null ){
        	 this.initError = true;
        	 initText.append( FilterUtils.outputTextCentered("Initialization failure! Parameters not found!", "*" ) );

        }else{
        	try{
        		this.strictControl = Boolean.parseBoolean( param3 );
        		this.allowURLRewriting = Boolean.parseBoolean( param2 );        	
        		this.sessionRenewalThreshold = Integer.parseInt( param1 );
        		
        		if( this.sessionRenewalThreshold <= 0 ){
        			this.initError = true;
        			initText.append( FilterUtils.outputTextCentered("Initialization failure! Parameter value incorrect!", "*" ) );
        			
        		}else{
	
            		initText.append( FilterUtils.outputTextCentered("URL Rewriting allowed: " + this.allowURLRewriting , "*" ) );
            		initText.append( FilterUtils.outputTextCentered("Session renewal threshold: " + this.sessionRenewalThreshold , "*" ) );
            		initText.append( FilterUtils.outputTextCentered("Strict session control: " + this.strictControl , "*" ) );
            		initText.append( FilterUtils.outputTextCentered("Error page URL: " + this.errorURL , "*" ) );

            		initText.append( FilterUtils.outputTextCentered("", "*" ) );
            		initText.append( FilterUtils.outputTextCentered("Initialization successful!", "*" ) );
        		}
        		
        	}catch( NumberFormatException e ){
        		 this.initError = true;
        		 initText.append( FilterUtils.outputTextCentered("Initialization failure! Parameter value incorrect!", "*" ) );
        	}

        }
       	
		initText.append( FilterUtils.outputTextDelimiter(false, "*" ) );
		filterConfigObj.getServletContext().log( initText.toString() );
    }
   

    public void doFilter( ServletRequest req , ServletResponse res , FilterChain chain ) throws ServletException,IOException{
   	
    	
    	HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) res;
    	HttpSession session = request.getSession(false);
    	
    	if( !this.initError ){
    		
        	//If session ID comes from URL rewriting it may be an attack
        	//So as an additional option we use a parameter to allow or disallow it
            if ( request.isRequestedSessionIdFromURL() && !allowURLRewriting ){
            	rejectRequest( request, response );
                return;
            }
            
            //If session is null we can skip the entire process, as the attack isn't possible
            if( session != null ){

            	if( request.getParameter("jsessionid") != null || request.getAttribute("jsessionid") != null ){
            		rejectRequest( request, response );
                    return;
            	}
            	
            	if( session.getAttribute( "prevRequests" ) == null ){
            		session.setAttribute( "prevRequests" , this.sessionRenewalThreshold );
            	}
            	
            	String doneRequests = session.getAttribute( "prevRequests" ).toString();
            	
        		int requests = Integer.parseInt( doneRequests );
        		
        		if( requests >= this.sessionRenewalThreshold ){
        			//Backup old session data, create a new one, then restore backup
        			session.setAttribute( "prevRequests", 1 );
                    Map<String,Object> temp = new ConcurrentHashMap<String,Object>();
                    Enumeration<String> e = session.getAttributeNames();
                    
                    while ( e != null && e.hasMoreElements() ) {
                            String name = (String) e.nextElement();
                            Object value = session.getAttribute( name );
                            temp.put( name , value );
                    }

        			session.invalidate();        			
        			session = request.getSession(true);

					for ( Map.Entry<String, Object> stringObjectEntry : temp.entrySet() ){
						session.setAttribute( stringObjectEntry.getKey() , stringObjectEntry.getValue() );
					}
  


        		}else{
        			requests++;
        			session.setAttribute( "prevRequests" , requests );
        		}
        		
        		//This additional controls improve overall security but they may give false positives due
        		//to user's network configuration (proxies,NAT,etc)
        		if( this.strictControl ){

        			if( session.getAttribute("user-agent") == null ){
        				session.setAttribute("user-agent", request.getHeader("user-agent") );
        			}
        			
        			if( session.getAttribute("userip") == null ){
        				session.setAttribute("userip", request.getRemoteAddr() );
        			}
        			
        			String req_agent = request.getHeader("user-agent");
            		String sess_agent = session.getAttribute("user-agent").toString();
            		
            		String req_ip = request.getRemoteAddr();
            		String sess_ip = session.getAttribute("userip").toString();

            		if( !req_agent.equals(sess_agent) ){
            			session.invalidate();
            			
            			StringBuilder initText = new StringBuilder( FilterUtils.outputTextDelimiter(true, "*" ) );
            	 		initText.append( FilterUtils.outputTextCentered("Servlet Misc Filters - Session Fixation Protection Filter", "*" ) );
            	 		initText.append( FilterUtils.outputTextCentered("User-agent conflict detected,possible threat. Session invalidated" , "*" ) );
            	 		initText.append( FilterUtils.outputTextDelimiter(false, "*" ) );
            			filterConfigObj.getServletContext().log( initText.toString() );
            	 		
            	 		rejectRequest( request, response );
            	 		return;

            		}
        			
            		if( !req_ip.equals(sess_ip) ){
            			session.invalidate();
            			
            			StringBuilder initText = new StringBuilder( FilterUtils.outputTextDelimiter(true, "*" ) );
            	 		initText.append( FilterUtils.outputTextCentered("Servlet Misc Filters - Session Fixation Protection Filter", "*" ) );
            	 		initText.append( FilterUtils.outputTextCentered("IP conflict detected, possible threat. Session invalidated" , "*" ) );
            	 		initText.append( FilterUtils.outputTextDelimiter(false, "*" ) );
            			filterConfigObj.getServletContext().log( initText.toString() );
            	 		
            	 		rejectRequest( request, response );
            	 		return;

            		}
        		}
            }
            
    	}else{
    		
    		StringBuilder errorText = new StringBuilder( FilterUtils.outputTextDelimiter(true, "-" ) );
    		errorText.append( FilterUtils.outputTextCentered("Session Fixation Protection Filter: Couldn't process session due to init errors!" , "|" ) );
			errorText.append( FilterUtils.outputTextDelimiter(false, "-" ) );
			
			filterConfigObj.getServletContext().log( errorText.toString() );
    	}
    	
    	chain.doFilter(request, response);
    }
    
    private void rejectRequest( HttpServletRequest request , HttpServletResponse response ) throws ServletException,IOException{
    	if( this.errorURL != null ){
    		request.setAttribute("errorMessage", "Security Error detected!");
    		request.getRequestDispatcher(this.errorURL).forward(request, response);

    	}else{

			response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR , "Security Error detected!" );
			
    	}
    	
    }
    
    public void destroy() {
    }
}