package com.example.googlemapstest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	EditText fromEdit;
	EditText toEdit;
	EditText nearbyEdit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button = (Button)findViewById(R.id.button1);
		 fromEdit = (EditText)findViewById(R.id.editText1);
		toEdit = (EditText)findViewById(R.id.editText2);
		nearbyEdit = (EditText)findViewById(R.id.editText3);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
					    Uri.parse("http://maps.google.com/maps?saddr="+fromEdit.getText().toString()+"&daddr="+toEdit.getText().toString()));
					startActivity(intent);
				
				
			}
		});
		
		Button nearby = (Button)findViewById(R.id.button2);
		nearby.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uri = "http://maps.google.com/maps?q=restaurants near "+nearbyEdit.getText().toString();
				startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
				
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
