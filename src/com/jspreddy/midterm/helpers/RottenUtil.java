/**
 * Midterm
 * Filename: RottenUtil.java
 * Sai Phaninder Reddy Jonnala
 */

package com.jspreddy.midterm.helpers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RottenUtil {
	static public class MoviesJSONParser{
		
		public static ArrayList<RottenMovieObject> parseMovies(String in) throws JSONException{
			ArrayList<RottenMovieObject> movieList = new ArrayList<RottenMovieObject>();
			
			JSONObject root = new JSONObject(in);
			
			if(!root.isNull("movies")){
				//its an array of movies, parse one by one.
				JSONArray moviesJSONArray = root.getJSONArray("movies");
				for(int i=0;i<moviesJSONArray.length(); i++){
					JSONObject movieJSONObject = moviesJSONArray.getJSONObject(i);
					RottenMovieObject movieObj = parseMovie(movieJSONObject);
					movieList.add(movieObj);
				}
			}
			else{
				//attempt to parse the root as movie object
				RottenMovieObject movieObj = parseMovie(root);
				movieList.add(movieObj);
			}
			
			return movieList;
			
		}
	
		public static RottenMovieObject parseMovie(JSONObject movieJSONObject){
			RottenMovieObject movieObj = new RottenMovieObject();
			if(!movieJSONObject.isNull("id")){
				try {
					movieObj.setId(movieJSONObject.getString("id"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!movieJSONObject.isNull("title")){
				try {
					movieObj.setTitle(movieJSONObject.getString("title"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(!movieJSONObject.isNull("year")){
				try {
					movieObj.setYear(movieJSONObject.getInt("year"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(!movieJSONObject.isNull("genres")){
				try{
					JSONArray array = movieJSONObject.getJSONArray("genres");
					for(int i=0;i<array.length();i++){
						movieObj.setGenres(array.getString(i));
					}
				}
				catch(JSONException e){
					
				}
				
			}
			
			if(!movieJSONObject.isNull("mpaa_rating")){
				try {
					movieObj.setMpaa(movieJSONObject.getString("mpaa_rating"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(!movieJSONObject.isNull("runtime")){
				try{
					movieObj.setRuntime(movieJSONObject.getInt("runtime"));
				}
				catch(JSONException e){
					
				}
			}
			
			if(!movieJSONObject.isNull("release_dates")){
				try {
					if(!movieJSONObject.getJSONObject("release_dates").isNull("theater") ){
						movieObj.setRelease_date(movieJSONObject.getJSONObject("release_dates").getString("theater"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(!movieJSONObject.isNull("ratings")){
				try {
					if(!movieJSONObject.getJSONObject("ratings").isNull("critics_rating")){
						movieObj.setCritics_rating(movieJSONObject.getJSONObject("ratings").getString("critics_rating"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(!movieJSONObject.getJSONObject("ratings").isNull("critics_score")){
						movieObj.setCritics_score(movieJSONObject.getJSONObject("ratings").getInt("critics_score"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(!movieJSONObject.getJSONObject("ratings").isNull("audience_rating")){
						movieObj.setAudience_rating(movieJSONObject.getJSONObject("ratings").getString("audience_rating"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(!movieJSONObject.getJSONObject("ratings").isNull("audience_score")){
						movieObj.setAudience_score(movieJSONObject.getJSONObject("ratings").getInt("audience_score"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(!movieJSONObject.isNull("posters")){
				try {
					if(!movieJSONObject.getJSONObject("posters").isNull("thumbnail")){
						movieObj.setImg_thumbnail(movieJSONObject.getJSONObject("posters").getString("thumbnail"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(!movieJSONObject.getJSONObject("posters").isNull("profile")){
						movieObj.setImg_profile(movieJSONObject.getJSONObject("posters").getString("profile"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(!movieJSONObject.getJSONObject("posters").isNull("detailed")){	
						movieObj.setImg_detailed(movieJSONObject.getJSONObject("posters").getString("detailed"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(!movieJSONObject.getJSONObject("posters").isNull("original")){
						movieObj.setImg_original(movieJSONObject.getJSONObject("posters").getString("original"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(!movieJSONObject.isNull("links")){
				try {
					if(!movieJSONObject.getJSONObject("links").isNull("alternate")){
						movieObj.setLink_alternate(movieJSONObject.getJSONObject("links").getString("alternate"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return movieObj;
		}
	
	}
}
