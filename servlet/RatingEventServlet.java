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
 * Servlet implementation class RatingEventServlet
 */
@WebServlet("/RatingEventServlet")
public class RatingEventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			System.out.println("here");
		String title = request.getParameter(StringConstants.TITLE);
		db_storage ds = (db_storage) request.getSession().getAttribute(StringConstants.DATA);
		//update the rating total and count fields of the movie and add a rated event to logged in user
		ds.changeRating(title, Integer.parseInt(request.getParameter(StringConstants.RATING)));
		//write back the new average rating
		response.getWriter().write(Long.toString(ds.getMovie(title).getAverageRating()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
