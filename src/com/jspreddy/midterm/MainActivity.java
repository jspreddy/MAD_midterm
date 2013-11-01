/**
 * Midterm
 * Filename: MainActivity.java
 * Sai Phaninder Reddy Jonnala
 */
package com.jspreddy.midterm;

import java.io.IOException;
import java.io.InputStream;
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
import org.xml.sax.SAXException;

import com.jspreddy.midterm.helpers.Config;
import com.jspreddy.midterm.helpers.Constants;
import com.jspreddy.midterm.helpers.FavApiObject;
import com.jspreddy.midterm.helpers.FavUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Context context;
	String uid;
	String[] listItems = {
			"My Favorite Movies",
			"Box Office Movies",
			"In Theaters Movies",
			"Opening Movies",
			"Upcoming Movies",
			"Favorite Statistics"
			};
	ListView lvMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lvMain = (ListView) findViewById(R.id.lvMain);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.main_list_item,listItems);
		lvMain.setAdapter(adapter);
		lvMain.setOnItemClickListener( new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(i == 5){
					Intent activity_statistics = new Intent(MainActivity.this,StatisticsActivity.class);
					startActivity(activity_statistics);
				}
				else
				{
					Intent activity_movies = new Intent(MainActivity.this,MoviesActivity.class);
					activity_movies.putExtra("MODE", i);
					startActivity(activity_movies);
				}
			}
			
		});
		
		context = this.getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.sharedPrefFile,Context.MODE_PRIVATE);
		
		String defaultValue = "";
		//read shared pref
		String uname_shared_pref = sharedPref.getString(Constants.uname_key, defaultValue);
		if(uname_shared_pref == "")
		{
			Intent username_activity = new Intent(MainActivity.this, UsernameActivity.class);
			startActivity(username_activity);
			finish();
		}
	
		//get uid from username
		uid=Config.getUid(uname_shared_pref);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.settings_clearAllFavs:
			new AsyncClearFavs().execute();
			break;
		case R.id.settings_username:
			Intent username_activity = new Intent(MainActivity.this, UsernameActivity.class);
			startActivity(username_activity);
			finish();
			break;
		case R.id.settings_Exit:
				finish();
			break;
		}
		return false;
	}

	
	public class AsyncClearFavs extends AsyncTask<Void, Void, FavApiObject>{

		@Override
		protected FavApiObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://cci-webdev.uncc.edu/~mshehab/api-rest/favorites/deleteAllFavorites.php");

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
			if(result != null){
				Toast.makeText(context, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}

}
