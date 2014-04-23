package com.example.gallery.GridView;

import java.io.File;
import java.util.ArrayList;

import com.example.simpleapp.R;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GridViewActivity extends Activity{

	private GridView gridView;
	private GridViewAdapter customGridAdapter;
	private Intent intent;
	private String path;
	private File targetFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview);

		intent = this.getIntent();
		path = intent.getStringExtra("path");
		targetFile = new File(path);
		
		gridView = (GridView) findViewById(R.id.gridView);
		customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, getData());
		gridView.setAdapter(customGridAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(GridViewActivity.this, position + "#Selected",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	private ArrayList<ImageItem> getData() {
		final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
		// retrieve String drawable array
		File[] dirs = targetFile.listFiles();
		for (File file : dirs)
		{
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			bitmap = Bitmap.createScaledBitmap(bitmap, 45, 80, true);
			bitmap = Bitmap.createScaledBitmap(bitmap, 90, 160, true);
			
			imageItems.add(new ImageItem(bitmap, file.getName()));
			//bitmap.recycle();
		}

		return imageItems;

	}
}
