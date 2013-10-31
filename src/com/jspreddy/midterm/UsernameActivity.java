package com.jspreddy.midterm;

import com.jspreddy.midterm.helpers.Constants;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UsernameActivity extends Activity {

	private EditText etUsername;
	private Button btnSetUname; 
	SharedPreferences sharedPref;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_username);
		
		context = this.getApplicationContext();
		sharedPref = context.getSharedPreferences(Constants.sharedPrefFile,Context.MODE_PRIVATE);
		
		String defaultValue = "";
		//read shared pref
		String uname_shared_pref = sharedPref.getString(Constants.uname_key, defaultValue);
		
		etUsername = (EditText) findViewById(R.id.etUsername);
		
		if(uname_shared_pref != "")
		{
			this.etUsername.setText(uname_shared_pref);
		}
		
		this.btnSetUname = (Button) this.findViewById(R.id.btnSetUname);
		this.btnSetUname.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String uname = etUsername.getText().toString();
				if(uname.isEmpty() ){
					etUsername.setError("Enter Username");
				}
				else{
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(Constants.uname_key, uname);
					editor.commit();
					
					Intent main_activity = new Intent(UsernameActivity.this, MainActivity.class);
					startActivity(main_activity);
					finish();
				}
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
