package com.example.wifi;

import com.example.simpleapp.R;
import android.support.v4.app.Fragment;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class WifiActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi);

		initView();
		//if (savedInstanceState == null) {
		//	getSupportFragmentManager().beginTransaction()
		//			.add(R.id.container, new PlaceholderFragment()).commit();
		//}
	}

	void initView()
	{
		TabHost thost = getTabHost();
		Intent intent = new Intent();
		intent.setClass(WifiActivity.this, ConnectDevicesTabWidget.class);
		TabSpec ts = thost.newTabSpec("tab1");
		ts.setContent(intent);
		ts.setIndicator("連線裝置");
		thost.addTab(ts);
		
		intent = new Intent();
		intent.setClass(WifiActivity.this, RecieveListTabWidget.class);
		TabSpec ts2 = thost.newTabSpec("tab2");
		ts2.setContent(intent);
		ts2.setIndicator("接收清單");
		thost.addTab(ts2);
		
		thost.setCurrentTab(0);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wifi, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_wifi, container,
					false);
			return rootView;
		}
	}

}
