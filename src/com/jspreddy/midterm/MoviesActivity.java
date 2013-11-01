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
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class MoviesActivity extends Activity {
	
	int mode=0;
	private Context context;
	private String uid;
	ListView lvMovies;
	ProgressDialog pd;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movies);
		
		lvMovies = (ListView) findViewById(R.id.lvMovies);
		
		
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
			new AsyncGetBOMovies().execute();
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
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
	
	public class AsyncGetBOMovies extends AsyncTask<Void, Void, ArrayList<RottenMovieObject>>{

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
		protected ArrayList<RottenMovieObject> doInBackground(Void... arg0) {
			try {
				URL url = new URL("http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?apikey=r77k8ra37t6q4hgk9974qm4j&limit=50");
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
		}

	}
	
}
