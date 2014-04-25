package com.example.gallery.FileList;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.DateFormat;

import com.example.gallery.GridView.GridViewActivity;
import com.example.simpleapp.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class FileListActivity extends ListActivity {

	private File currentDir;
	private FileArrayAdapter adapter;
	private List<Albumb> dir;
	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	

	@Override
	protected void onStart() {
		super.onStart();
		
		dir = new ArrayList<Albumb>();
		currentDir = new File("/sdcard/DCIM/");
		fill(currentDir);
		currentDir = new File("/sdcard/Pictures/");
		fill(currentDir);
		
		adapter = new FileArrayAdapter(FileListActivity.this, R.layout.filelist, dir);
		this.setListAdapter(adapter);
	}

	private void fill(File f) {
		File[] dirs = f.listFiles();
		Integer numberInteger = dirs.length;
		Log.e("file number", numberInteger.toString());
		this.setTitle("Current Dir: " + f.getName());
		List<Albumb> fls = new ArrayList<Albumb>();
		char firstChar;
		int buf;
		String name;
		Date lastModDate;
		DateFormat formater;
		String date_modify;
		Boolean imageBoolean = false;
		String[] spaceStrings;
		String num_item;
		Integer count = 0;
		ArrayList<String> nameStrings = new ArrayList<String>();
		Integer index;
		
		try {
			for (File ff : dirs) {
				name = ff.getName();
				lastModDate = new Date(ff.lastModified());
				formater = DateFormat.getDateTimeInstance();
				date_modify = formater.format(lastModDate);
				firstChar = name.charAt(0);
				if (firstChar != '.')
				{
					/*
					 * Note: Remove this
					 * name.equalsIgnoreCase("Personal" if u
					 * want to list all ur sd card file and folder
					 */
					if (ff.isDirectory()) {
						File[] fbuf = ff.listFiles();
						buf = 0;
						if (fbuf != null) 
						{
							buf = fbuf.length;
						}
						num_item = String.valueOf(buf);
						if (buf != 0)
						{
							num_item = num_item + " items";
							dir.add(new Albumb(ff.getName(), num_item, date_modify, ff
								.getAbsolutePath(), "directory_icon"));
						}
						
					} 
					else {
						/*
						 * Note: Remove this
						 * f.getName().equalsIgnoreCase("Personal"
						 * if u want to list all ur sd card file and folder
						 */
							
						/*fls.add(new Albumb(ff.getName(), ff.length() + " Byte",
									date_modify, ff.getAbsolutePath(), "file_icon"));*/
						Log.e("image", "yes");
						imageBoolean = true;
					}
				}
			}
			if(imageBoolean)
			{
				
				lastModDate = new Date(f.lastModified());
				formater = DateFormat.getDateTimeInstance();
				date_modify = formater.format(lastModDate);
				
				for(File file : dirs)
				{
					name = file.getName();
					spaceStrings = name.split("\\.");
					if(spaceStrings[spaceStrings.length - 1].equals("jpg"))
					{
						count++;
						Log.e("in", count.toString());
					}
				}
				
				if(count > 1)
					num_item = String.valueOf(count) + "items";
				else
					num_item = String.valueOf(count) + "item";
				dir.add(new Albumb(f.getName(), num_item, date_modify, f.getAbsolutePath(), "directory_icon"));
				
			}
		} 
		catch (Exception e) 
		{}
		Collections.sort(dir);	
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Albumb o = adapter.getItem(position);
		if (o.getImage().equalsIgnoreCase("directory_icon")
				|| o.getImage().equalsIgnoreCase("directory_up")) {
			//currentDir = new File(o.getPath());
			Log.e("click path", o.getPath());
			//fill(currentDir);
			intent = new Intent(FileListActivity.this, GridViewActivity.class);
			intent.putExtra("path", o.getPath());
			startActivity(intent);
		}
	}
}
