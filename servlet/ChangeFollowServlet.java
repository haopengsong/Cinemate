package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.db_storage;
import data.StringConstants;

/**
 * Servlet implementation class ChangeFollowServlet
 */
@WebServlet("/ChangeFollowServlet")
public class ChangeFollowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("here hhh");
		try{
		String username = request.getParameter(StringConstants.USERNAME);
		Boolean toFollow = Boolean.parseBoolean(request.getParameter(StringConstants.TO_FOLLOW));
		
		db_storage ds = (db_storage) request.getSession().getAttribute(StringConstants.DATA);
		//if the logged in user wants to follow this username
		if (toFollow) {
			System.out.println("here add" + username);
			ds.addFollowing(username); 
			}
		//else the logged in user wants to unfollow this username
		else { 
			System.out.println("here remove" + username);
			ds.removeFollowing(username);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
