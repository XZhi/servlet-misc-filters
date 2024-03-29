package es.biohazardwtf.miscfilters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//IDEA: http://tutorials.jenkov.com/java-web-apps/web-app-maintenance.html
public class MaintenanceFilter implements Filter{

	
	private FilterConfig filterConfigObj;
	private String redirectURL;
	private AtomicInteger requestCounter;
	private String adminUsername;
	private String adminPassword;
	private String maintenanceURL;
	
	private static final boolean MAINTENANCE_MODE = true;
	
	private boolean maintenanceMode = false;
	
	public void init(FilterConfig config) throws ServletException{
		this.filterConfigObj = config;
		this.redirectURL = config.getInitParameter("redirectURL");
		this.adminUsername = config.getInitParameter("adminUsername");
		this.adminPassword = config.getInitParameter("adminPassword");
		this.maintenanceURL =  config.getInitParameter("maintenanceURL");
		
		this.requestCounter = new AtomicInteger(0);
		
		StringBuilder initText = new StringBuilder( FilterUtils.outputTextDelimiter(true, "*" ) );
		initText.append( FilterUtils.outputTextCentered("Servlet Misc Filters - Maintenance Filter", "*" ) );

		if( this.redirectURL != null && this.adminUsername != null && this.adminPassword != null){

			initText.append( FilterUtils.outputTextCentered("Redirect requests to: " + this.redirectURL , "*" ) );
			initText.append( FilterUtils.outputTextCentered("Admin username: " + this.adminUsername + "  Password: " + this.adminPassword, "*" ) );
			initText.append( FilterUtils.outputTextCentered("Maintenance panel URL: " + this.maintenanceURL , "*" ) );
			initText.append( FilterUtils.outputTextCentered("", "*" ) );
			initText.append( FilterUtils.outputTextCentered("Initialization successful!", "*" ) );

		}else{
			initText.append( FilterUtils.outputTextCentered("Initialization failure! Parameter not found!", "*" ) );
			 
		}
		 
		initText.append( FilterUtils.outputTextDelimiter(false, "*" ) );
		this.filterConfigObj.getServletContext().log( initText.toString() );

	}
	
	public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException, ServletException{
		
		HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) res;
  
    	if( this.maintenanceMode != MAINTENANCE_MODE ){
    		System.out.println("MAINTENANCE MODE OFF");    		
    		
    	}else{
    		System.out.println("MAINTENANCE MODE ON");    		
    		request.getRequestDispatcher( this.redirectURL ).forward( request, response );
    		return;
    		
    	}

//    	this.requestCounter.incrementAndGet();
//    	System.out.println( "VALOR PRE-dofilter:" + this.requestCounter.get() );
    	
    	chain.doFilter( request,response );
    	
//    	this.requestCounter.decrementAndGet();
//    	System.out.println( "VALOR POST-dofilter:" + this.requestCounter.get() );

	}

	public void destroy() {
	}
	
	
	private boolean checkMaintenanceMode (HttpServletRequest request ){
		
		boolean returnparam = false;
		
		String maintenanceParam = request.getParameter("maintenancemode");
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	//String forceParam = request.getParameter("force");
    	
    	if( maintenanceParam != null && username != null && password != null ){
    		//If the three needed parameters aren't null and their values are correct...
    		if( maintenanceParam.equals("maintenancemode") && this.adminUsername.equals(username) && this.adminPassword.equals(password) ){
    			this.maintenanceMode = Boolean.parseBoolean(maintenanceParam);
    			
    		}
    	}
    	
    	
		return returnparam;
	}

}
