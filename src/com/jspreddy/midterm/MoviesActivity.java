package com.jspreddy.midterm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.xml.sax.SAXException;

import com.jspreddy.midterm.helpers.Config;
import com.jspreddy.midterm.helpers.Constants;
import com.jspreddy.midterm.helpers.FavApiObject;
import com.jspreddy.midterm.helpers.FavUtil;
import com.jspreddy.midterm.helpers.RottenMovieObject;
import com.jspreddy.midterm.helpers.RottenUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MoviesActivity extends Activity {
	
	int mode=0;
	private Context context;
	private String uid;
	ListView lvMovies;
	ProgressDialog pd;
	
	String url_box_office="http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?apikey=r77k8ra37t6q4hgk9974qm4j&limit=50";
	String url_in_theaters="http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=r77k8ra37t6q4hgk9974qm4j&limit=50";
	String url_opening="http://api.rottentomatoes.com/api/public/v1.0/lists/movies/opening.json?apikey=r77k8ra37t6q4hgk9974qm4j&limit=50";
	String url_upcoming="http://api.rottentomatoes.com/api/public/v1.0/lists/movies/upcoming.json?apikey=r77k8ra37t6q4hgk9974qm4j&limit=50";
	
	LruCache<String, Bitmap> mMemoryCache;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movies);
		
		lvMovies = (ListView) findViewById(R.id.lvMovies);
		
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount() / 1024;
			}
		};
		
		context = this.getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.sharedPrefFile,Context.MODE_PRIVATE);
		
		String defaultValue = "";
		//read shared pref
		String uname_shared_pref = sharedPref.getString(Constants.uname_key, defaultValue);
		if(uname_shared_pref == "")
		{
			Intent username_activity = new Intent(MoviesActivity.this, UsernameActivity.class);
			startActivity(username_activity);
			finish();
		}
		uid=Config.getUid(uname_shared_pref);
		
		if (getIntent().getExtras() != null) {
			mode = getIntent().getExtras().getInt("MODE");
		}
		else{mode=0;}
		
		switch(mode){
		case 0:
			new AsyncGetAllFavs().execute();
			break;
		case 1:
			new AsyncGetMovies().execute(url_box_office);
			break;
		case 2:
			new AsyncGetMovies().execute(url_in_theaters);
			break;
		case 3:
			new AsyncGetMovies().execute(url_opening);
			break;
		case 4:
			new AsyncGetMovies().execute(url_upcoming);
			break;
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	public class AsyncGetAllFavs extends AsyncTask<Void, Void, FavApiObject>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd=new ProgressDialog(MoviesActivity.this);
			pd.setCancelable(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Loading Movies");
			pd.show();
		}

		@Override
		protected FavApiObject doInBackground(Void... params) {
			
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://cci-webdev.uncc.edu/~mshehab/api-rest/favorites/getAllFavorites.php");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("uid", uid));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        InputStream in = response.getEntity().getContent();
		        
		        return FavUtil.FavSAXParser.getFavApiObject(in);
		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    } catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(FavApiObject result) {
			pd.dismiss();
			if(result != null){
				Toast.makeText(context, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public class AsyncGetMovies extends AsyncTask<String, Void, ArrayList<RottenMovieObject>>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd=new ProgressDialog(MoviesActivity.this);
			pd.setCancelable(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Loading Movies");
			pd.show();
		}
		
		@Override
		protected ArrayList<RottenMovieObject> doInBackground(String... arg) {
			try {
				URL url = new URL(arg[0]);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				int statusCode = con.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = reader.readLine();
					while (line != null) {
						sb.append(line);
						line = reader.readLine();
					}
					
					ArrayList<RottenMovieObject> movielist = RottenUtil.MoviesJSONParser.parseMovies(sb.toString());
					
					return movielist;
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<RottenMovieObject> result) {
			super.onPostExecute(result);
			pd.dismiss();
			MoviesAdapter ma = new MoviesAdapter(MoviesActivity.this, result);
			lvMovies.setAdapter(ma);
		}

	}

	class MoviesAdapter extends ArrayAdapter<RottenMovieObject>{
		Context context;
		ArrayList<RottenMovieObject> moviesList;
		
		public MoviesAdapter(Context context, ArrayList<RottenMovieObject> moviesList) {
			super(context, R.layout.movies_list_item, R.id.tvTitle, moviesList);
			this.context = context;
			this.moviesList = moviesList;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.movies_list_item, parent, false);
			
			ImageView ivThumbnail = (ImageView) row.findViewById(R.id.ivThumbnail);
			TextView tvTitle = (TextView) row.findViewById(R.id.tvTitle);
			TextView tvYear = (TextView) row.findViewById(R.id.tvYear);
			TextView tvMpaa = (TextView) row.findViewById(R.id.tvMpaa);
			ImageView ivCritRating = (ImageView) row.findViewById(R.id.ivCritRating);
			ImageView ivAudRating = (ImageView) row.findViewById(R.id.ivAudRating);
			ivCritRating.setScaleType(ScaleType.CENTER_INSIDE);
			ivAudRating.setScaleType(ScaleType.CENTER_INSIDE);
			ivThumbnail.setScaleType(ScaleType.CENTER_INSIDE);
			
			RottenMovieObject item = this.moviesList.get(position);
			tvTitle.setText(item.getTitle());
			tvYear.setText(item.getYear()+"");
			tvMpaa.setText(item.getMpaa());
			
			new AsyncDownloadImage().execute(new ImageLoad(item.getImg_profile(), ivThumbnail));
			
			if(item.getCritics_rating().equals("Fresh")){
				ivCritRating.setImageResource(R.drawable.fresh);
			}
			else if(item.getCritics_rating().equals("Certified Fresh")){
				ivCritRating.setImageResource(R.drawable.certified_fresh);
			}
			else if(item.getCritics_rating().equals("Rotten")){
				ivCritRating.setImageResource(R.drawable.rotten);
			}
			
			if(item.getAudience_rating().equals("Upright")){
				ivAudRating.setImageResource(R.drawable.upright);
			}
			else if(item.getAudience_rating().equals("Spilled")){
				ivAudRating.setImageResource(R.drawable.spilled);
			}
			
			return row;
		}
		
		
	}
	
	class ImageLoad{
		String url="";
		ImageView iv;
		public ImageLoad(String url, ImageView iv){
			this.url = url;
			this.iv=iv;
		}
	}
	
	class AsyncDownloadImage extends AsyncTask<ImageLoad,Void,Bitmap>{
		
		ImageLoad il;
		@Override
		protected Bitmap doInBackground(ImageLoad... params) {
			il=params[0];
			URL url;
			try {
				Bitmap bmp = getBitmapFromMemCache(il.url);
				if(bmp == null){
					url = new URL(il.url);
					bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
					if(bmp==null){
						return BitmapFactory.decodeResource(getResources(), R.drawable.poster_not_found);
					}else{
						addBitmapToMemoryCache(il.url, bmp);
						return bmp;
					}
				}
				else{
					return bmp;
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return BitmapFactory.decodeResource(getResources(), R.drawable.poster_not_found);
		}

		@Override
		protected void onPostExecute(Bitmap img) {
			super.onPostExecute(img);
			il.iv.setImageBitmap(img);
		}
		
		
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}
	
	
}
