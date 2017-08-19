package servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.*;

import data.db_storage;
import data.StringConstants;
import data.User;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("h");
		
		try {
			db_storage ds =  new db_storage();
			Map<String, User> m = ds.getusermap();
			for (String name: m.keySet()){
	            String key =name;
	            User u = m.get(name);  
	            System.out.println("username: "+ key + " " + u.getFullname());  
			} 
			request.getSession().setAttribute(StringConstants.DATA, ds);
			String username = (String)request.getParameter(StringConstants.USERNAME);
			String password = (String)request.getParameter(StringConstants.PASSWORD);
			System.out.println(username);
			System.out.println(password);
			//if it is a valid username
			if (ds.validUsername(username)){
				//correct password
				if (ds.correctPassword(username, password)){
					ds.setLoggedInUser(username);
					 
				}
				//incorrect password
				else{
					response.getWriter().write("Incorrect password");
					//response.getWriter().flush();
				}
			}
			//invalid username
			else{
				response.getWriter().write("Invalid username");
				//response.getWriter().flush();
				
			}	
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			response.getWriter().write(e.getMessage());
			//response.getWriter().flush();
			e.printStackTrace();
		}
	}
}
