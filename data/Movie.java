package data;

import java.util.ArrayList;
import java.util.List;

public class Movie {
	private String title;//
	private String image;//
	private String director;//
	private String description;//
	private int year;//
	private List<Actor> actors;//
	private List<String> writers;//
	private int numRatings;//
	private int totalRating;//
	private List<String> genres;//
	private int id;
	private Double imdb_rating;
	private String imdbID;
	public Movie() {
		genres = new ArrayList<>();
		actors = new ArrayList<>();
		writers = new ArrayList<>();
	}
	public String getIMDBID(){
		return this.imdbID;
	}
	public void setIMDBID(String id){
		this.imdbID = id;
	}
	//METHODS
	public void addGenre(String genre) {
		//System.out.println("g "+genre);
		genres.add(genre);
	}
	
	public void addActor(Actor actor){
		actors.add(actor);
	}
	
	public void addWriter(String actor){
		writers.add(actor);
	}
	
	public void incrementRatingCount(){
		numRatings++;
	}
	
	public void updateRatingTotal(int rating){
		totalRating += rating;
	}
	
	//GETTERS
	public Double getimdb_rating(){
		return imdb_rating;
	}
	public long getAverageRating(){
		if(numRatings == 0){
			//System.out.println(numRatings+" num rating");
			return 0;
		}
		else 
		{
			//System.out.println(numRatings+" num rating");
			return totalRating/numRatings;
		}
	}
	
	public int getTotalRating(){
		return totalRating;
	}
	
	public int getRatingCount(){
		return numRatings;
	}
	
	public String getGenre(){
		String genress = "";
		for(int i=0;i<genres.size();i++){
			genress = genress + genres.get(i) + " ";
		}
		return genress;
	}
	
	public List<String> getWriters(){
		return writers;
	}

	public String getTitle() {
		return title;
	}
	
	public String getImage(){
		return image;
	}

	public String getDirector() {
		return director;
	}

	public String getDescription() {
		return description;
	}

	public int getYear() {
		return year;
	}
	public List<Actor> getActors() {
		return actors;
	}
	public int getID(){
		return id;
	}
	
	
	
	//SETTERS
	public void setImdbRating(Double i){
		
		this.imdb_rating = i;
	}
	
	public void setId(int i){
		this.id = i;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}

	public void setWriters(List<String> writers) {
		this.writers = writers;
	}

	public void setRatingTotal(int rating) {
		totalRating = rating;
		//System.out.println(totalRating+" total");
	}
	
	public void setRatingCount(int num) {
		numRatings = num;
		//System.out.println(numRatings+" num rating");
	}


	public void setImage(String image){
		this.image = image;
	}
}
