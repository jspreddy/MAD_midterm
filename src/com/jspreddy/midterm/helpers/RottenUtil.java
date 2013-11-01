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
			JSONArray movieJSONArray = root.getJSONArray("movies");
			
			for(int i=0;i<movieJSONArray.length(); i++){
				JSONObject movieJSONObject = movieJSONArray.getJSONObject(i);
				RottenMovieObject movieObj =new RottenMovieObject();
				
				movieObj.setId(movieJSONObject.getString("id"));
				movieObj.setTitle(movieJSONObject.getString("title"));
				movieObj.setYear(movieJSONObject.getInt("year"));
				movieObj.setMpaa(movieJSONObject.getString("mpaa_rating"));
				movieObj.setRuntime(movieJSONObject.getInt("runtime"));
				
				movieObj.setRelease_date(movieJSONObject.getJSONObject("release_dates").getString("theater"));
				
				movieObj.setCritics_rating(movieJSONObject.getJSONObject("ratings").getString("critics_rating"));
				movieObj.setCritics_score(movieJSONObject.getJSONObject("ratings").getInt("critics_score"));
				movieObj.setAudience_rating(movieJSONObject.getJSONObject("ratings").getString("audience_rating"));
				movieObj.setAudience_score(movieJSONObject.getJSONObject("ratings").getInt("audience_score"));
				
				//movieObj.setSynopsis(movieJSONObject.getString("synopsis"));
				
				movieObj.setImg_thumbnail(movieJSONObject.getJSONObject("posters").getString("thumbnail"));
				movieObj.setImg_profile(movieJSONObject.getJSONObject("posters").getString("profile"));
				movieObj.setImg_detailed(movieJSONObject.getJSONObject("posters").getString("detailed"));
				movieObj.setImg_original(movieJSONObject.getJSONObject("posters").getString("original"));
				
				movieObj.setLink_alternate(movieJSONObject.getJSONObject("links").getString("alternate"));
				
				movieList.add(movieObj);
			}
			
			return movieList;
			
		}
	}
}
