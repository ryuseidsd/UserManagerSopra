package gdsd.com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class formAction
 */
@WebServlet("/formRegister")
public class register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String VIEW_PAGES_URL="/WEB-INF/register.jsp";
	public static final String EMAIL = "email";
	public static final String PWD = "pwd";
	public static final String PWD2 = "pwd2";
	public static final String NAME = "name";
	
	private String validateEmail( String email ) {
		String ret = "";
		if ( email != null && email.trim().length() != 0 ) {
			 if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
			 	return "Veuillez saisir une adresse mail valide";
			 }
		} else {
			return  "L'adresse mail est obligatoire";
		}
		return ret;
	}
	
	private String validatePwd(String pwd, String pwd2) {
		String ret = "";
		if ( pwd != null && pwd.trim().length() >= 6 ) 
		{
			if ( pwd2 != null && pwd2.trim().length() >= 6 ) 
			{
				if ( !pwd.equals(pwd2) ) 
				{
					ret = "Les deux mots de passe rentrés sont différents";
				}
			} else {
				ret = "Le mot de passe doit faire plus de 6 caractères";
			}
		} else {
			ret = "Le mot de passe doit faire plus de 6 caractères";
		}
		return ret;
	}
	
    /**
     * Default constructor. 
     */
    public register() {
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String msgErrorEmail="";
		 String msgErrorPwd="";
		 String msgErrorpwd2="";
		 String msgErrorName="";
		 String msgValidation="";
		 String email="";
		 String pwd="";
		 String cpwd="";
		 String name="";
		 request.setAttribute("msgErrorEmail", msgErrorEmail);
		 request.setAttribute("msgErrorPwd", msgErrorPwd);
		 request.setAttribute("msgErrorpwd2", msgErrorpwd2);
		 request.setAttribute("msgErrorName", msgErrorName);
		 request.setAttribute("msgValidation", msgValidation);
		 request.setAttribute(EMAIL, email);
		 request.setAttribute(PWD, pwd);
		 request.setAttribute(PWD2, cpwd);
		 request.setAttribute(NAME, name);
		 request.setAttribute("errorStatus", true);
		
		 // Display page
		 this.getServletContext().getRequestDispatcher(VIEW_PAGES_URL).forward(request,response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		HttpSession session = request.getSession();		
		
		// Read parameters values
		Map<String, String> form = new HashMap<String, String>();
		form.put(EMAIL, request.getParameter(EMAIL));
		form.put(PWD, request.getParameter(PWD));
		form.put(PWD2, request.getParameter(PWD2));
		form.put(NAME, request.getParameter(NAME));		

		 // Validate parameters
		 Map<String, String> errors = new HashMap<String, String>();
		 
		 // Email address parameter
		 String errMsg = validateEmail(form.get(EMAIL)) ;
		 if(errMsg!=null){
		  errors.put(EMAIL, errMsg);
		 }		 
		 // Password parameters...
		 errMsg = validatePwd(form.get(PWD),form.get(PWD2));
		 if(errMsg!=null){
			  errors.put(PWD, errMsg);
		 }	
		 
		 // Name parameter...
		 String msgErrorName = "";
		
		 // Form validation
		 String msgValidation="";
		 boolean errorStatus;
		 if(errors.get(EMAIL) == "" && msgErrorName == "" && errors.get(PWD) == ""){
			 msgValidation = "Inscription réussie";
			 errorStatus = false;
		 }
		 else {
			 msgValidation = "Inscription impossible";
			 errorStatus = true;
		 }
		 
		  // Set validation feedback attributes
		  request.setAttribute("msgValidation", msgValidation);
		  request.setAttribute("form", form);
		  request.setAttribute("errors", errors);
		  request.setAttribute("errorStatus", errorStatus);
		 
		  if(!errorStatus) {
			  User newUser=null;
			  newUser=new User(form.get(NAME),form.get(EMAIL),form.get(PWD));
			  request.setAttribute("newUser", newUser);
			  
			  Map<String, User> users = (HashMap<String, User>) session.getAttribute("users" );
			  if(users==null){
				 users = new HashMap<String, User>();
			  }
			  users.put( newUser.getEmail(), newUser );
			  session.setAttribute( "users", users );
		  }
		  
		  // Display page again !
		  this.getServletContext().getRequestDispatcher(VIEW_PAGES_URL).include(request, response);
	}
	
}
