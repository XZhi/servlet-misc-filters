package es.biohazardwtf.miscfilters;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Test extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void init( ServletConfig config ) throws ServletException {
		super.init( config );
	}
	
	public void doGet ( HttpServletRequest request , HttpServletResponse response ) throws IOException, ServletException{
		doPost(request, response);
	}
	
	public void doPost( HttpServletRequest request , HttpServletResponse response ) throws IOException, ServletException{
		
		String page = request.getParameter("pagina").toString();
		String destination;
		
		switch( Page.toPage(page) ){
				case MAIN:

						request.getSession().setAttribute("attribute1" , "UNO");
						request.getSession().setAttribute("attribute2" , "DOS");
						request.getSession().setAttribute("attribute3" , "TRES");		
					
								
					destination =  "/WEB-INF/main.jsp";
					break;
					
				case INICIO:
					
					destination = "/index.jsp";
					break;
					
				default:
					//Codigo de error generico,si aparece es que se ha solicitado una operacion no contemplada
					destination = "/WEB-INF/error_general.jsp";
					request.setAttribute( "error" , "unknown" );
					break;
		
		}
	
		//Obtenemos el requestDispatcher y lanzamos el forward
		ServletContext sc = this.getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher( destination );
		rd.forward( request , response );
	}
	
	
	//Tipo ENUM para usarse como switch case
	public enum Page{
		MAIN,INICIO,ERROR;

	    public static Page toPage(String str){
	        try {
	        	str= str.toUpperCase();
	            return valueOf(str);
	            
	        }catch (Exception e){
	            return ERROR;
	        }
	    }   
	}

}
