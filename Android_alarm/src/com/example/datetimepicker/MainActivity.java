
package com.example.datetimepicker;

import java.lang.Character.UnicodeBlock;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datetimepicker.dateTime;;

public class MainActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	AlarmManager alarmManager = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		findViewById(R.id.ButtonDemo).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view.getId() == R.id.ButtonDemo)
			showDateTimeDialog();
	}

	private void showDateTimeDialog() {
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(this);
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final dateTime mDateTimePicker = (dateTime) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
		// Check is system is set to use 24h time (this doesn't seem to work as expected though)
		final String timeS = android.provider.Settings.System.getString(getContentResolver(), android.provider.Settings.System.TIME_12_24);
		final boolean is24h = !(timeS == null || timeS.equals("12"));
		
		// Update demo TextViews when the "OK" button is clicked 
		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDateTimePicker.clearFocus();
				// TODO Auto-generated method stub
				((TextView) findViewById(R.id.Date)).setText(mDateTimePicker.get(Calendar.YEAR) + "/" + (mDateTimePicker.get(Calendar.MONTH)+1) + "/"
						+ mDateTimePicker.get(Calendar.DAY_OF_MONTH));
				if (mDateTimePicker.is24HourView()) {
					((TextView) findViewById(R.id.Time)).setText(mDateTimePicker.get(Calendar.HOUR_OF_DAY) + ":" + mDateTimePicker.get(Calendar.MINUTE));
				} else {
					((TextView) findViewById(R.id.Time)).setText(mDateTimePicker.get(Calendar.HOUR) + ":" + mDateTimePicker.get(Calendar.MINUTE) + " "
							+ (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM"));
				}
				Calendar cal = Calendar.getInstance();
//				Toast.makeText(MainActivity.this,mDateTimePicker.get(Calendar.MONTH),Toast.LENGTH_LONG).show();
//				Toast.makeText(MainActivity.this,mDateTimePicker.get(Calendar.YEAR),Toast.LENGTH_LONG).show();
//				Toast.makeText(MainActivity.this,mDateTimePicker.get(Calendar.DAY_OF_MONTH),Toast.LENGTH_LONG).show();
//				Toast.makeText(MainActivity.this,mDateTimePicker.get(Calendar.HOUR_OF_DAY),Toast.LENGTH_LONG).show();
//				Toast.makeText(MainActivity.this,mDateTimePicker.get(Calendar.MINUTE),Toast.LENGTH_LONG).show();
//				
				cal.set(Calendar.MONTH, mDateTimePicker.get(Calendar.MONTH));
				cal.set(Calendar.YEAR,mDateTimePicker.get(Calendar.YEAR));
				cal.set(Calendar.DAY_OF_MONTH,mDateTimePicker.get(Calendar.DAY_OF_MONTH));
				cal.set(Calendar.HOUR_OF_DAY,mDateTimePicker.get(Calendar.HOUR_OF_DAY));
				cal.set(Calendar.MINUTE,mDateTimePicker.get(Calendar.MINUTE));
				
				Intent myIntent = new Intent(MainActivity.this,com.example.datetimepicker.BReceiver.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this.getApplicationContext(), 0, myIntent, 0);
				
				
				alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
				Toast.makeText(MainActivity.this, "Alaram is set", Toast.LENGTH_LONG).show();
				mDateTimeDialog.dismiss();
			}
		});

		// Cancel the dialog when the "Cancel" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDateTimeDialog.cancel(); 
			}
		});

		// Reset Date and Time pickers when the "Reset" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDateTimePicker.reset();
			}
		});
		
		
		// Setup TimePicker
		mDateTimePicker.setIs24HourView(is24h);
		// No title on the dialog window
		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();
	}
	
}