/**
 * Midterm
 * Filename: MovieActivity.java
 * Sai Phaninder Reddy Jonnala
 */
package com.jspreddy.midterm;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MovieActivity extends Activity {

	int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie);
		
		
		
		if (getIntent().getExtras() != null) {
			id = getIntent().getExtras().getInt("id");
		}
		else{id=0;}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
