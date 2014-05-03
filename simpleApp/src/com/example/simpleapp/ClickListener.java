package com.example.simpleapp;

import com.example.camera.CameraActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ClickListener {
	
	private Activity activity;
	
	public ClickListener(Activity activity)
	{
		this.activity = activity;
	}
	
	public OnClickListener intentClickListener(final Context context, final Class<?> cls) {
		OnClickListener resultClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, cls);
				activity.startActivity(intent);
			}
		};
		
		return resultClickListener;
	}	
}
