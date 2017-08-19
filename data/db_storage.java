package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.json.*;

import org.json.JSONException;

public class db_storage extends db_r {
	
	private User loggedInUser;
	private db_w dbw;
	public db_storage() throws Exception {
		super();
		dbw = new db_w();
	}
	//searching

	public Set<Movie> searchByTitle(String title_){
		String title = this.noAndsign(title_);
		if(titleToMovies.containsKey(title)){
			return titleToMovies.get(title.toLowerCase());
		}
		else {
			//api this movie write back to db
			Set<Movie> results = new HashSet<>();
			URL omdb;
			try {
				omdb = new URL("http://www.omdbapi.com/?s="+title);
				BufferedReader in = new BufferedReader(new InputStreamReader(omdb.openStream()));
				String movie_info = in.readLine();
				JSONObject jo = new JSONObject(movie_info);
				JSONArray ja = jo.getJSONArray("Search");
				System.out.println(ja.toString());
				Movie m = new Movie();
				//get all movies id and then search again using omdbapi by id
				for(int i=0;i<ja.length();i++){
					String movie_id = ja.getJSONObject(i).getString("imdbID");
					m = getMovieInfobyId(movie_id);

					results.add(m);
					//System.out.println(movie_id);
				}
				titleToMovies.put(title, results);
				return results;

			} catch (IOException | JSONException e) {

				e.printStackTrace();
			}

		}
		return null;
	}
	public Movie getMovieInfobyId(String id){
		Movie m = new Movie();
		try {
			URL omdb = new URL("http://www.omdbapi.com/?i="+id);
			BufferedReader in = new BufferedReader(new InputStreamReader(omdb.openStream()));
			String inputLine = in.readLine();
			JSONObject jo = new JSONObject(inputLine);
			m.setTitle(this.noAndsign(jo.getString("Title")));
		//	System.out.println(jo.getString("Title").toString());
			m.setYear(Integer.parseInt(jo.getString("Year").substring(0, 4)));
			this.parseGenres(m, jo.getString("Genre"));
			m.setDirector(jo.getString("Director").toString());
			//System.out.println("director: "+jo.getString("Director").toString());
			if(jo.getString("Writer").toString().equals("N/A")){
				m.getWriters().add("N/A");
			}
			else {
				this.parseWriter(m, jo.getString("Writer"));
			}
			this.parseActor(m, jo.getString("Actors"));
			m.setDescription(jo.getString("Plot"));
			//System.out.println(jo.getString("Poster"));
			m.setImage(jo.getString("Poster"));
			m.setIMDBID(id);
			m.setImdbRating(Double.parseDouble(jo.getString("imdbRating")));
			//System.out.println(Double.parseDouble(jo.getString("imdbRating")));
			moviesMap.put(this.noAndsign(jo.getString("Title").trim()), m);
			return m;
		}catch (Exception e){
			e.printStackTrace();
		}

		return null;
	}
	public Set<Movie> searchByActor(String actor){
		Set<Movie> m_result = new HashSet<>();
			try{
				System.out.println(actor);
				Movie m = new Movie();
				//find movie title that the actor is known for
				String encoded_query = URLEncoder.encode(actor, "UTF-8");
				System.out.println(encoded_query);
			//	String urlstring = 
				//wait 4s
//				try {
//				    Thread.sleep(4000);
//				} catch(InterruptedException ex) {
//				    Thread.currentThread().interrupt();
//				}
				URL infoUrl = new URL("https://api.themoviedb.org/3/search/person?api_key=6fcdd94b2c3de6dca333cce3a2789227&language=en-US&query="+ encoded_query +"&page=1&include_adult=false");
				//URLConnection urlConnection = infoUrl.openConnection();
				//System.out.println(urlstring);
				BufferedReader in = new BufferedReader(new InputStreamReader(infoUrl.openStream()));
				String inputLine=in.readLine();
				//get movie known for
				System.out.println(inputLine);
				JSONObject jo = new JSONObject(inputLine);
				JSONArray ja = jo.getJSONArray("results");
				for(int k=0;k<ja.getJSONObject(0).getJSONArray("known_for").length();k++){
					String title_known = ja.getJSONObject(0).getJSONArray("known_for").getJSONObject(k).getString("original_title");
					//check details
					String encodeTitle = URLEncoder.encode(title_known, "UTF-8");
					m_result.add(this.getMovieBytitle(encodeTitle));
					m = this.getMovieBytitle(encodeTitle);
					this.addObjectToMap(actorToMovies, actor, m);
				}
				return actorToMovies.get(actor);
			}catch(Exception e){

			}
		
		return null;
	}
	public Movie getMovieBytitle(String title){
		Movie m = new Movie();
		try {
			URL omdb = new URL("http://www.omdbapi.com/?t="+title);
			BufferedReader in = new BufferedReader(new InputStreamReader(omdb.openStream()));
			String inputLine = in.readLine();
			JSONObject jo = new JSONObject(inputLine);
			
			m.setTitle(this.noAndsign(jo.getString("Title")));
			//System.out.println(jo.getString("Title").toString());
			m.setYear(Integer.parseInt(jo.getString("Year").substring(0, 4)));
			this.parseGenres(m, jo.getString("Genre"));
			m.setDirector(jo.getString("Director").toString());
			//System.out.println("director: "+jo.getString("Director").toString());
			if(jo.getString("Writer").toString().equals("N/A")){
				m.getWriters().add("N/A");
			}
			else {
				this.parseWriter(m, jo.getString("Writer"));
			}
			this.parseActor(m, jo.getString("Actors"));
			m.setDescription(jo.getString("Plot"));
			//System.out.println(jo.getString("Poster"));
			m.setImage(jo.getString("Poster"));
			m.setIMDBID(jo.getString("imdbID"));
			m.setImdbRating(Double.parseDouble(jo.getString("imdbRating")));
			//System.out.println(Double.parseDouble(jo.getString("imdbRating")));
			moviesMap.put(jo.getString("Title").trim(), m);
			return m;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public String noAndsign(String s){
		if(s.contains("&")){
		  int loc_and = s.indexOf("&");
		  s = s.substring(0, loc_and-1) + " and"+ s.substring(loc_and+1,s.length());
		  System.out.println(s);
		}
		return s;
	}
	public Set<User> searchForUser(String username){
		Set<User> userSets = new HashSet<>();
		Set<User> usernames = usernameToUsers.get(username.toLowerCase());
		Set<User> fnames = firstNameToUsers.get(username.toLowerCase());
		Set<User> lnames = lastNameToUsers.get(username.toLowerCase());

		if (usernames != null) userSets.addAll(usernames);
		if(fnames != null) userSets.addAll(fnames);
		if (lnames != null) userSets.addAll(lnames);

		return userSets;
	}
	//getters

	/////////////////////////
	public Map<String, User> getusermap(){
		return usersMap;
	}

	public User getUser(String usernanme){
		return usersMap.get(usernanme);
	}

	public User getLoggedInUser(){
		return loggedInUser;
	}

	public Movie getMovie(String title){
		return moviesMap.get(title);
	}
	//setters
	public void setLoggedInUser(String username){
		loggedInUser = usersMap.get(username);
		dbw.setLoggedInUser(loggedInUser);
	}

	//check if a username is valid
	public Boolean validUsername(String username){
		return usersMap.containsKey(username);
	}
	//check if a password is correct
	public Boolean correctPassword(String username, String password){
		return usersMap.get(username).getPassword().equals(password);
	}
	//modify data methods
	public void addEvent(String actionTaken, String title, Integer ratingGiven) throws JSONException{
		//save the newe event to the xml file
		dbw.addEvent(actionTaken, title, ratingGiven);
		//create event object and add it to logged in user's feed
		 LocalDateTime now = LocalDateTime.now();
		Event event = new Event();
		event.setAction(actionTaken);
		event.setMovie(moviesMap.get(title));
		event.settime(now.toString());
		if (ratingGiven != null){
			event.setRating(ratingGiven);
		}

		event.setUsername(loggedInUser.getUsername());

		if (event.getAction().equals(StringConstants.ACTION_RATED)){
			event.setActionIcon(actionToIcon.get(event.getRatingToDisplay()+StringConstants.ACTION_RATED));
		}
		else{
			event.setActionIcon(actionToIcon.get(event.getAction()));
		}

		loggedInUser.getFeed().add(event);
	}
	public void removeFollowing(String username) throws JSONException{
		dbw.removeFollowing(getUser(username));
	}

	public void addFollowing(String username) throws JSONException{
		dbw.addFollowing(getUser(username));
	}
	public void addUser(User user){
		//add the new user to all the appropriate maps
		usersMap.put(user.getUsername(), user);
		addObjectToMap(usernameToUsers, user.getUsername().toLowerCase(), user);
		addObjectToMap(firstNameToUsers, user.getFName().toLowerCase(), user);
		addObjectToMap(lastNameToUsers, user.getLName().toLowerCase(), user);

		//save the new user to the db

		dbw.createNewUser(user);
		//assign id
		user.setId(usersMap.size());

	}
	//change the average rating of the movie
	public void changeRating(String title, Integer rating) throws JSONException{
		//add a rated event to the logged in user
		addEvent(StringConstants.ACTION_RATED, title, rating);
		//update the rating of the movie object
		Movie movie = moviesMap.get(title);
		movie.incrementRatingCount();
		movie.updateRatingTotal(rating);
		//save the rating changes to the xml file
		dbw.changeRating(title, movie);
	}
	protected <T> void addObjectToMap(Map<String, Set<T>> map, String key, T object){
		//if the map already contains the provided key, retrieve the value (which is a set) and add the new object
		if (map.containsKey(key)) map.get(key).add(object);
		//else create a new set with the object, and add the provided key and created set to the map
		else{
			Set<T> objects = new HashSet<>();
			objects.add(object);
			map.put(key, objects);
		}
	}
	public static void main(String[] args) throws Exception{
		db_storage db = new db_storage(); 
		//db.searchByTitle("star+wars");
		db.searchByActor("vin diesel");
	}
	////	

}
