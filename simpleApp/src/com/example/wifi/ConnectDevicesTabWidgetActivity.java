package com.example.wifi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import com.example.simpleapp.R;
import com.example.wifi.DeviceListFragment.DeviceActionListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ConnectDevicesTabWidgetActivity extends Activity implements ChannelListener, DeviceActionListener {
	public static final String TAG = "wifidirectdemo";
    private WifiP2pManager manager;
    private boolean isWifiP2pEnabled = false;
    private boolean retryChannel = false;

    private final IntentFilter intentFilter = new IntentFilter();
    private Channel channel;
    private BroadcastReceiver receiver = null;
    
	private WifiManager wiFiManager;
	private ToggleButton WifiButn;
	private ImageButton searchWifiButton;
	private WifiP2pDevice device;
	private TextView selfWifiNameTextView;
	private TextView selfStatusTextView;
	private WifiP2pConfig config = new WifiP2pConfig();
	private List peers = new ArrayList();
	
	public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }
	
	public void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.connectdeviceslayout);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		
	    wiFiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);	    
	    device = new WifiP2pDevice();
	    manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
	    config.deviceAddress = device.deviceAddress;
		
		manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		channel = manager.initialize(this, getMainLooper(), null);
		//receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
		
		//WifiInfo wifiInfo = wiFiManager.getConnectionInfo();
		//System.out.println(wifiInfo.getSSID());
		
		WifiButn = (ToggleButton) findViewById(R.id.WifiToggleButton);
		searchWifiButton = (ImageButton) findViewById(R.id.WifiSearchImageButton);
		//selfWifiNameTextView = (TextView) findViewById(R.id.selfNametextView);
		//selfStatusTextView = (TextView) findViewById(R.id.selfStatusTextView);		
		//selfWifiNameTextView.setText("123");
		//selfStatusTextView.setText("Avaliable");
		
		InitialWifiToggleButton();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		Log.d("123", "21");
	    android.app.Fragment fragment = getFragmentManager().findFragmentById(R.id.frag_detail);
	    fragment.onActivityResult(requestCode, resultCode, data);
	}
	
	public void InitialWifiToggleButton()
	{
		if(wiFiManager.isWifiEnabled())
		{
			WifiButn.setChecked(true);
		}
		if(!wiFiManager.isWifiEnabled())
		{
			WifiButn.setChecked(false);
		}
	}
	
	public void	onWifiToggleClicked(View view) {
		//Is the toggle on?
		boolean	on = ((ToggleButton)view).isChecked();
		if(on){
			if (!wiFiManager.isWifiEnabled()) 
			{
				wiFiManager.setWifiEnabled(true);
				searchWifiButton.setClickable(true);
			}
			//Toast.makeText(MainActivity.this, "Sound turned on!" ,Toast.LENGTH_LONG).show();
		}
		else {
			if (wiFiManager.isWifiEnabled()) 
			{
				wiFiManager.setWifiEnabled(false);
				searchWifiButton.setClickable(false);
			}
			//Toast.makeText(MainActivity.this, "Sound turned off!",Toast.LENGTH_LONG).show();
		}
	}
	
	public void onSearchButtonClicked(View view)
	{
		if (!isWifiP2pEnabled) {
            Toast.makeText(ConnectDevicesTabWidgetActivity.this, R.string.p2p_off_warning,
                    Toast.LENGTH_SHORT).show();
        }
        final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
                .findFragmentById(R.id.frag_list);
        fragment.onInitiateDiscovery();
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(ConnectDevicesTabWidgetActivity.this, "Discovery Initiated",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(ConnectDevicesTabWidgetActivity.this, "Discovery Failed : " + reasonCode,
                        Toast.LENGTH_SHORT).show();
            }
        });
//		manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
//		    @Override
//		    public void onSuccess() {
//		        //...
//		    }
//
//		    @Override
//		    public void onFailure(int reasonCode) {
//		        //...
//		    }
//		});
//		
//		manager.connect(channel, config, new WifiP2pManager.ActionListener() {
//
//		    @Override
//		    public void onSuccess() {
//		        //success logic
//		    }
//
//		    @Override
//		    public void onFailure(int reason) {
//		        //failure logic
//		    }
//		});
	}
	
	/** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
	
	/**
     * Remove all peers and clear all fields. This is called on
     * BroadcastReceiver receiving a state change event.
     */
    public void resetData() {
        DeviceListFragment fragmentList = (DeviceListFragment) getFragmentManager()
                .findFragmentById(R.id.frag_list);
        DeviceDetailFragment fragmentDetails = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        if (fragmentList != null) {
            fragmentList.clearPeers();
        }
        if (fragmentDetails != null) {
            fragmentDetails.resetViews();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.action_items, menu);
        return true;
    }

	@Override
    public void showDetails(WifiP2pDevice device) {
        DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.showDetails(device);

    }

    @Override
    public void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(ConnectDevicesTabWidgetActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnect() {
        final DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.resetViews();
        manager.removeGroup(channel, new ActionListener() {

            @Override
            public void onFailure(int reasonCode) {
                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);

            }

            @Override
            public void onSuccess() {
                fragment.getView().setVisibility(View.GONE);
            }

        });
    }

    @Override
    public void onChannelDisconnected() {
        // we will try once more
        if (manager != null && !retryChannel) {
            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            resetData();
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);
        } else {
            Toast.makeText(this,
                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void cancelDisconnect() {

        /*
         * A cancel abort request by user. Disconnect i.e. removeGroup if
         * already connected. Else, request WifiP2pManager to abort the ongoing
         * request
         */
        if (manager != null) {
            final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
                    .findFragmentById(R.id.frag_list);
            if (fragment.getDevice() == null
                    || fragment.getDevice().status == WifiP2pDevice.CONNECTED) {
                disconnect();
            } else if (fragment.getDevice().status == WifiP2pDevice.AVAILABLE
                    || fragment.getDevice().status == WifiP2pDevice.INVITED) {

                manager.cancelConnect(channel, new ActionListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(ConnectDevicesTabWidgetActivity.this, "Aborting connection",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(ConnectDevicesTabWidgetActivity.this,
                                "Connect abort request failed. Reason Code: " + reasonCode,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
}


