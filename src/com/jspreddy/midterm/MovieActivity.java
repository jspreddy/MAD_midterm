/**
 * Midterm
 * Filename: MovieActivity.java
 * Sai Phaninder Reddy Jonnala
 */
package com.jspreddy.midterm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

import com.jspreddy.midterm.helpers.Constants;
import com.jspreddy.midterm.helpers.ImageLoad;
import com.jspreddy.midterm.helpers.RottenMovieObject;
import com.jspreddy.midterm.helpers.RottenUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class MovieActivity extends Activity {

	int id;
	ProgressDialog pd;
	RottenMovieObject movie;
	Context context;
	
	TextView tvTitle;
	TextView tvReleaseDate;
	TextView tvMpaa;
	TextView tvRuntime;
	TextView tvGenre;
	TextView tvAudRat;
	TextView tvCritRat;
	
	ImageView ivMain;
	ImageView ivAud;
	ImageView ivCrit;
	
	ImageView ivBack;
	ImageView ivWeb;
	ImageView ivStar;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie);
		
		this.tvTitle = (TextView) findViewById(R.id.tv_MovieActivity_Title);
		this.tvReleaseDate = (TextView) findViewById(R.id.tv_MovieActivity_ReleaseDate);
		this.tvMpaa = (TextView) findViewById(R.id.tv_MovieActivity_Mpaa);
		this.tvRuntime = (TextView) findViewById(R.id.tv_MovieActivity_Runtime);
		this.tvGenre = (TextView) findViewById(R.id.tv_MovieActivity_Genre);
		this.tvAudRat = (TextView) findViewById(R.id.tv_MovieActivity_AudRat);
		this.tvCritRat = (TextView) findViewById(R.id.tv_MovieActivity_CritRat);
		
		this.ivMain = (ImageView) findViewById(R.id.iv_MovieActivity_Main);
		this.ivAud = (ImageView) findViewById(R.id.iv_MovieActivity_Aud);
		this.ivCrit = (ImageView) findViewById(R.id.iv_MovieActivity_Crit);
		
		this.ivBack = (ImageView) findViewById(R.id.iv_MovieActivity_Back);
		this.ivWeb = (ImageView) findViewById(R.id.iv_MovieActivity_Web);
		this.ivStar = (ImageView) findViewById(R.id.iv_MovieActivity_Star);
		
		this.ivMain.setScaleType(ScaleType.FIT_CENTER);
		this.ivAud.setScaleType(ScaleType.FIT_CENTER);
		this.ivCrit.setScaleType(ScaleType.FIT_CENTER);
		
		context=this.getApplicationContext();
		
		this.ivBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("myData1", "Data 1 value");
				data.putExtra("myData2", "Data 2 value");
				// Activity finished ok, return the data
				setResult(RESULT_OK, data);
				finish();
			}
		});
		
		this.ivWeb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!movie.getLink_alternate().equals("")){
					Intent I = new Intent(MovieActivity.this,MovieWebActivity.class);
					I.putExtra("url", movie.getLink_alternate());
					startActivity(I);
				}
			}
		});
		
		if (getIntent().getExtras() != null) {
			id = getIntent().getExtras().getInt("id");
		}
		else{id=0;}
		Log.d("DEBUG","movie id: "+id);
		new AsyncGetMovie().execute("http://api.rottentomatoes.com/api/public/v1.0/movies/"+id+".json?apikey="+Constants.API_KEY);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exit_menu, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.settings_Exit:
				finish();
				System.exit(0);
			break;
		}
		return false;
	}

	public class AsyncGetMovie extends AsyncTask<String, Void, RottenMovieObject>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(pd==null){
				pd=new ProgressDialog(MovieActivity.this);
			}
			if(!pd.isShowing()){
				pd.setCancelable(false);
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setMessage("Loading Movies");
				pd.show();
			}
		}
		
		@Override
		protected RottenMovieObject doInBackground(String... arg) {
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
					
					return RottenUtil.MoviesJSONParser.parseMovie(new JSONObject(sb.toString()) );
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
		protected void onPostExecute(RottenMovieObject result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(result!=null){
				movie = result;
				
				tvTitle.setText(result.getTitle());
				tvReleaseDate.setText(result.getRelease_date());
				tvMpaa.setText(result.getMpaa());
				tvRuntime.setText(minutesToTimeString(result.getRuntime()));
				String temp = result.getGenres();
				int len = (temp.length() > Constants.GenreStringLength) ? Constants.GenreStringLength : temp.length();
				temp = temp.substring(0, len)+"...";
				tvGenre.setText(temp);
				tvAudRat.setText(""+result.getAudience_score()+"%");
				tvCritRat.setText(""+result.getCritics_score()+"%");
				
				
				new AsyncDownloadImage().execute(new ImageLoad(result.getImg_detailed(), ivMain));
				
				if(result.getCritics_rating().equals("Fresh")){
					ivCrit.setImageResource(R.drawable.fresh);
				}
				else if(result.getCritics_rating().equals("Certified Fresh")){
					ivCrit.setImageResource(R.drawable.certified_fresh);
				}
				else if(result.getCritics_rating().equals("Rotten")){
					ivCrit.setImageResource(R.drawable.rotten);
				}
				
				if(result.getAudience_rating().equals("Upright")){
					ivAud.setImageResource(R.drawable.upright);
				}
				else if(result.getAudience_rating().equals("Spilled")){
					ivAud.setImageResource(R.drawable.spilled);
				}
				
//				ivBack;
//				ivStar;
				
			}
			else{
				Toast.makeText(context, "No Movie to display", Toast.LENGTH_SHORT).show();
			}
		}

	}

	
	class AsyncDownloadImage extends AsyncTask<ImageLoad,Void,Bitmap>{
		
		ImageLoad il;
		@Override
		protected Bitmap doInBackground(ImageLoad... params) {
			il=params[0];
			URL url;
			try {
				url = new URL(il.url);
				Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				if(bmp==null){
					return BitmapFactory.decodeResource(getResources(), R.drawable.poster_not_found);
				}else{
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
	
	public String minutesToTimeString(int minutes){
		
		if(minutes < 0){
			minutes = 0;
		}
		
		int rem = minutes % 60;
		int quo = (int)(minutes / 60);
		
		return quo+"hr. "+rem+"min.";
	}
	
}
