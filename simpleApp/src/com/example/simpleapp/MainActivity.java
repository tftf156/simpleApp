package com.example.simpleapp;

import com.example.camera.CameraActivity;
import com.example.gallery.FileList.FileListActivity;
import com.example.wifi.*;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {

	private ImageView cameraImageView;
	private ImageView galleryImageView;
	private ImageView wifiImageView;
	private ClickListener clickListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/		
		
		cameraImageView = (ImageView) findViewById(R.id.cameraImageView);
		galleryImageView = (ImageView) findViewById(R.id.galleryImageView);
		wifiImageView = (ImageView) findViewById(R.id.wifiImageView);
		clickListener = new ClickListener(MainActivity.this);
		
		cameraImageView.setOnClickListener(
				clickListener.intentClickListener(MainActivity.this, CameraActivity.class));
		galleryImageView.setOnClickListener(
				clickListener.intentClickListener(MainActivity.this, FileListActivity.class));
		wifiImageView.setOnClickListener(
				clickListener.intentClickListener(MainActivity.this, WifiActivity.class));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
