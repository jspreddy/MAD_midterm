/**
 * Midterm
 * Filename: MovieWebActivity.java
 * Sai Phaninder Reddy Jonnala
 */
package com.jspreddy.midterm;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MovieWebActivity extends Activity {

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_web);
		
		WebView wv=(WebView) findViewById(R.id.wvMain);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				view.loadUrl(url);
				return false;
			}
		});
		
		if(getIntent().getExtras()!=null){
			String s = getIntent().getExtras().getString("url");
			wv.loadUrl(s);
		}
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
}
