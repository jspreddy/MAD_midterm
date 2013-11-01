/**
 * Midterm
 * Filename: StatisticsActivity.java
 * Sai Phaninder Reddy Jonnala
 */
package com.jspreddy.midterm;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class StatisticsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
