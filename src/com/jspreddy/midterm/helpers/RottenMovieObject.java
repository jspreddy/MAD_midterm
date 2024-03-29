/**
 * Midterm
 * Filename: RottenMovieObject.java
 * Sai Phaninder Reddy Jonnala
 */
package com.jspreddy.midterm.helpers;

public class RottenMovieObject {
	String id;
	String title;
	int year;
	String mpaa;
	int runtime;

	String release_date;
	
	String critics_rating;
	int critics_score;
	
	String audience_rating;
	int audience_score;
	
	//String synopsis;
	
	String img_thumbnail;
	String img_profile;
	String img_detailed;
	String img_original;
	
	String link_alternate;
	
	String genres;

	public RottenMovieObject(){
		this.id="";
		this.title="";
		this.year=0;
		this.mpaa="";
		this.runtime=0;
		this.release_date="";
		this.critics_rating="";
		this.critics_score=0;
		this.audience_rating = "";
		this.audience_score=0;
		this.img_detailed="";
		this.img_original="";
		this.img_profile="";
		this.img_thumbnail="";
		this.link_alternate="";
		this.genres="";
	}
	
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getYear() {
		return year;
	}

	public String getMpaa() {
		return mpaa;
	}

	public int getRuntime() {
		return runtime;
	}

	public String getRelease_date() {
		return release_date;
	}

	public String getCritics_rating() {
		return critics_rating;
	}

	public int getCritics_score() {
		return critics_score;
	}

	public String getAudience_rating() {
		return audience_rating;
	}

	public int getAudience_score() {
		return audience_score;
	}

	public String getImg_thumbnail() {
		return img_thumbnail;
	}

	public String getImg_profile() {
		return img_profile;
	}

	public String getImg_detailed() {
		return img_detailed;
	}

	public String getImg_original() {
		return img_original;
	}

	public String getLink_alternate() {
		return link_alternate;
	}

	public String getGenres() {
		return genres;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMpaa(String mpaa) {
		this.mpaa = mpaa;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}

	public void setCritics_rating(String critics_rating) {
		this.critics_rating = critics_rating;
	}

	public void setCritics_score(int critics_score) {
		this.critics_score = critics_score;
	}

	public void setAudience_rating(String audience_rating) {
		this.audience_rating = audience_rating;
	}

	public void setAudience_score(int audience_score) {
		this.audience_score = audience_score;
	}

	public void setImg_thumbnail(String img_thumbnail) {
		this.img_thumbnail = img_thumbnail;
	}

	public void setImg_profile(String img_profile) {
		this.img_profile = img_profile;
	}

	public void setImg_detailed(String img_detailed) {
		this.img_detailed = img_detailed;
	}

	public void setImg_original(String img_original) {
		this.img_original = img_original;
	}

	public void setLink_alternate(String link_alternate) {
		this.link_alternate = link_alternate;
	}

	public void setGenres(String genres) {
		if(this.genres.equals("")){
			this.genres += genres;
		}
		else{
			this.genres += ", "+genres;
		}
	}


}
