package com.badrul.qnitibox.old;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.badrul.qnitibox.Config;
import com.badrul.qnitibox.R;
import com.badrul.qnitibox.StaffOrder;

public class FridayBreakfast extends AppCompatActivity {

	ImageButton continueBtn;
	ImageButton eventBtn;
	String menuType = "Breakfast";
	String menuDay = "Friday";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friday_breakfast);

		continueBtn = (ImageButton) findViewById(R.id.continueBtn);
		eventBtn = (ImageButton) findViewById(R.id.eventBtn);

		continueBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				SharedPreferences sharedPreferences = FridayBreakfast.this.getSharedPreferences(Config.SHARED_PREF_NAME,
						Context.MODE_PRIVATE);

				// Creating editor to store values to shared preferences
				SharedPreferences.Editor editor = sharedPreferences.edit();

				// Adding values to editor

				editor.putString(Config.MENU_TYPE, menuType);
				editor.putString(Config.MENU_DAY, menuDay);

				// Saving values to editor
				editor.commit();

				Intent i = new Intent(FridayBreakfast.this, IndvOrder.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
			}
		});

		eventBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				SharedPreferences sharedPreferences = FridayBreakfast.this.getSharedPreferences(Config.SHARED_PREF_NAME,
						Context.MODE_PRIVATE);

				// Creating editor to store values to shared preferences
				SharedPreferences.Editor editor = sharedPreferences.edit();

				// Adding values to editor

				editor.putString(Config.MENU_TYPE, menuType);
				editor.putString(Config.MENU_DAY, menuDay);

				// Saving values to editor
				editor.commit();

				Intent i = new Intent(FridayBreakfast.this, StaffOrder.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
			}
		});
	}
}
