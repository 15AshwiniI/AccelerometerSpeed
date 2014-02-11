package com.example.accelerometerspeed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.text.format.Time;
import android.util.FloatMath;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {
	float dx =0;
	float dy =0;
	float dz =0;
	
	Time startTime;
	Time endTime;
	float totalAccel;
	float highAccel=0;
	long startTimeInMillis;
	long endTimeInMillis;
	public String data;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SensorManager manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		Sensor accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
		
		//calculating Start Time
		startTime= new Time();
		startTime.setToNow();
		startTimeInMillis= startTime.toMillis(false);
		toast("On Create StartTimeMillis: " + startTimeInMillis);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public void toast(CharSequence prompt)
	{
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, prompt, duration);
		toast.show();
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		TextView x= ((TextView)findViewById(R.id.textView1));
		TextView y= ((TextView)findViewById(R.id.textView2));
		TextView z= ((TextView)findViewById(R.id.textView3));
		
		TextView accel= ((TextView)findViewById(R.id.textView4));
		TextView haccel= ((TextView)findViewById(R.id.textView5));
		
		x.setText("X: " +(event.values[0]-dx));
		y.setText("Y: " +(event.values[1]-dy));
		z.setText("Z: " +(event.values[2]-dz));
		totalAccel = FloatMath.sqrt((event.values[0]-dx)*(event.values[0]-dx)
									+(event.values[1]-dx)*(event.values[1]-dx)
									+(event.values[2]-dx)*(event.values[2]-dx))
									- SensorManager.GRAVITY_EARTH;
		if(totalAccel>highAccel) highAccel = totalAccel;
		haccel.setText("H: "+ highAccel);
		accel.setText("Acceleration" +totalAccel);
	}
	
	public void calcSpeed(View v){
		System.out.println ("in Timer");
		endTime = new Time();
		endTime.setToNow();
		long endTimeInMillis = endTime.toMillis(false);
		
		toast("endTimer TimeInMillis: "+endTimeInMillis);
		long time = startTimeInMillis - endTimeInMillis;
		toast("Speed" + time*totalAccel);
	}
	
	private void writeToFile(View v) 
	{
		try{
		
		
	File sdCard = Environment.getExternalStorageDirectory();
	File directory = new File(sdCard.getAbsolutePath()+"/GPSDataDir");
	directory.mkdirs();
	File file = new File(directory, "velocityData"+".txt");
	FileOutputStream fou = new FileOutputStream(file);
	OutputStreamWriter osw = new OutputStreamWriter(fou);
	osw.write(data);
	 osw.flush();
     osw.close();
		}
		catch(FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	
}
		
		
	}


