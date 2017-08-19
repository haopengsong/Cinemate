package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.BiConsumer;
import java.io.*;
import org.json.*;
import org.w3c.dom.Node;

import java.net.*;

//write changes back to database
public class db_w {
	//logged in user
	private User loggedInUser;
	ParseConfig pc = new ParseConfig();
	Connection conn = null;
	Statement st = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	public void addFollowing(User u) throws JSONException{

		//add the user's username to the the loggedin user's following list
		loggedInUser.getFollowing().add(u.getUsername());
		//add the loggedin user's username to the user's followers list
		u.getFollowers().add(loggedInUser.getUsername());
		//need update the map in storage

		//write back to database
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://"+ pc.getAddress() +"/"+ pc.getDataBase() +"?user="+pc.getuser()+"&password="+pc.getPassword()+"&useSSL=false");

			//execute a query
			st = conn.createStatement();
			//String adding = u.getUsername();
			String adding = u.getUsername();

			String select_user = "select u.following from users u where u.user_id = "+"'" + loggedInUser.getId() + "'";
			//String select_beadded = "select u.following from users u where u.user_id = "+"'" + u.getId() + "'";
			System.out.println(select_user);
			//update loggedin's
			rs = st.executeQuery(select_user);
			
			if(rs.next()){
			
			String tochange = rs.getString("following");
			System.out.println(tochange);
			JSONArray js = new JSONArray(tochange);
			JSONObject jo = new JSONObject();
			jo.put("username", adding);
			js.put(jo);
			String updater = "update users u set u.following ="+"'"+ js.toString() + "'" +" where u.user_id = "+"'" + loggedInUser.getId() +"'";
			//System.out.println(updater);
			st.executeUpdate(updater);
			}
			//update beadded's 
			//need update the map in storage


		}catch (SQLException sqle) {
			System.out.println ("sssSQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println ("ClassNotFoundException: " + cnfe.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
	}


	public void removeFollowing(User u) throws JSONException{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://"+ pc.getAddress() +"/"+ pc.getDataBase() +"?user="+pc.getuser()+"&password="+pc.getPassword()+"&useSSL=false");

			//person to remove 
			String toremove = u.getUsername();
			//look for this person
			if(loggedInUser.getFollowing().contains(toremove)){
				//remove
				loggedInUser.getFollowing().remove(toremove);
				u.getFollowers().remove(loggedInUser.getUsername());
				//update database
				//execute a query
				st = conn.createStatement();
				//get old following list
				String sql_ = "select u.following from users u where u.user_id = "+"'" + loggedInUser.getId() + "'";
				System.out.println(sql_);
				rs = st.executeQuery(sql_);

				if(rs.next())
				{
					String tochange = rs.getString("following");
					JSONArray js = new JSONArray(tochange);
					for(int i=0;i<js.length();i++){
						//looking for a value that matches the remove name...
						if(js.getJSONObject(i).get("username").equals(toremove)){
							js.remove(i);
						}
					}
					String updater = "update users u set u.following ="+"'"+ js.toString() + "'" +" where u.user_id = "+"'" + loggedInUser.getId() +"'";
					//	System.out.println(updater);
					st.executeUpdate(updater);
				}
			}



		}catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println ("ClassNotFoundException: " + cnfe.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
	}
	public void setLoggedInUser(User user){
		loggedInUser = user;
	}
	//create new user and save back to database
	public void createNewUser(User u){
		System.out.println("got create user");
		//user info
		String sql = "insert into users (username,password,fname,lname,image,following,feed) values (?,?,?,?,?,?,?)";
		String username = u.getUsername();
		String pw = u.getPassword();
		String fname = u.getFName();
		String lname = u.getLName();
		String image = u.getImage();
		String feed = "[null]";
		String following = "[null]";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://"+ pc.getAddress() +"/"+ pc.getDataBase() +"?user="+pc.getuser()+"&password="+pc.getPassword()+"&useSSL=false");
	//execute a query


			ps = conn.prepareStatement(sql);
			ps.setString(1,username);
			ps.setString(2, pw);
			ps.setString(3, fname);
			ps.setString(4, lname);
			ps.setString(5, image);
			ps.setString(6, following);
			ps.setString(7, feed);
			ps.executeUpdate();
			
//			st = conn.createStatement();
//			String checkid  ="select u from users u where u.username="+"'" +u.getUsername()+ "'";
//			rs = st.executeQuery(checkid);
//			if(rs.next()){
//				u.setId(Integer.parseInt(rs.getString("user_id")));
//			}

		}catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println ("ClassNotFoundException: " + cnfe.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
	}
	public void addEvent(String action, String title, Integer rating) throws JSONException{
		//modifiy database
		JSONObject jo = new JSONObject();

		jo.put("action", action);
		jo.put("movie", title);
		if(rating!=null){
			jo.put("rating", rating);
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://"+ pc.getAddress() +"/"+ pc.getDataBase() +"?user="+pc.getuser()+"&password="+pc.getPassword()+"&useSSL=false");

			//execute a query
			st = conn.createStatement();

			String sql_ = "select u.feed from users u where u.user_id = "+"'" + loggedInUser.getId() + "'";
			rs = st.executeQuery(sql_);
			if(rs.next()){
			String feed = rs.getString("feed");
			JSONArray ja = new JSONArray(feed);
			ja.put(jo);
			String updater = "update users u set u.feed ="+"'"+ ja.toString() + "'" +" where u.user_id = "+"'" + loggedInUser.getId() +"'";
			//	System.out.println(updater);
			st.executeUpdate(updater);
			}
		}catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println ("ClassNotFoundException: " + cnfe.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}	
	}
	public void changeRating(String title, Movie m){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://"+ pc.getAddress() +"/"+ pc.getDataBase() +"?user="+pc.getuser()+"&password="+pc.getPassword()+"&useSSL=false");

			//execute a query
			st = conn.createStatement();
			//update totalrating
			String updater1 = "update movies m set m.rating_total ="+"'"+ m.getTotalRating() + "'" +" where u.user_id = "+"'" + m.getID() +"'";
			st.executeUpdate(updater1);
			//update number ratings
			String updater2 = "update movies m set m.rating_total ="+"'"+ m.getRatingCount() + "'" +" where u.user_id = "+"'" + m.getID() +"'";
			st.executeUpdate(updater2);
			String sql_ = "select u.feed from users u where u.user_id = "+"'" + loggedInUser.getId() + "'";
			rs = st.executeQuery(sql_);


		}catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println ("ClassNotFoundException: " + cnfe.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}	
	}
}
