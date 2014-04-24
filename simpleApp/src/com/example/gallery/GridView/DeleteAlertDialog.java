package com.example.gallery.GridView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class DeleteAlertDialog {

	private Builder builder;
	private GridViewAdapter gridViewAdapter;
	private GridView gridView;
	private DialogInterface.OnClickListener deleteClickListener;
	private DialogInterface.OnClickListener cancelClickListener;
	private static int INVISIBLE = 4;
	
	public DeleteAlertDialog(Context context, GridViewAdapter gridViewAdapter, GridView gridView)
	{
		this.builder = new AlertDialog.Builder(context);
		this.gridViewAdapter = gridViewAdapter;
		this.gridView = gridView;
	}
	
	public void onShow(final RelativeLayout functions)
	{
		deleteClickListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				gridViewAdapter.deleteAllSelectedItems(gridView);
				functions.setVisibility(INVISIBLE);
			}
		};
		
		cancelClickListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		};
		
		builder.setTitle("�R��");
		builder.setMessage("�T�w�n�R��?");
		builder.setNegativeButton("�R��", deleteClickListener);
		builder.setPositiveButton("����", cancelClickListener);
		builder.show();
	}
	
}
