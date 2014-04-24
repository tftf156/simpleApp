package com.example.gallery.GridView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.simpleapp.R;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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
			/* (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 */
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				customGridAdapter.changeState(position, gridView);
			}
		});

	}

	private ArrayList<ImageItem> getData() 
	{
		final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
		File[] dirs = targetFile.listFiles();
		
		for (File file : dirs)
		{
			Bitmap bitmap = getBitmap(Uri.fromFile(file));
			imageItems.add(new ImageItem(bitmap, file.getName()));
		}
		
		return imageItems;
	}
	
	public Bitmap getBitmap(Uri uri) 
	{
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, opts);
            in.close();
 
            int sampleSize = computeSampleSize(opts, -1, 128 * 128);
 
            in = getContentResolver().openInputStream(uri);
            opts = new BitmapFactory.Options();
            opts.inSampleSize = sampleSize;
 
            Bitmap bmp = BitmapFactory.decodeStream(in, null, opts);
            in.close();
 
            return bmp;
        } 
        catch (Exception err) 
        {
            return null;
        }
    }
 
    public static int computeSampleSize(BitmapFactory.Options options, 
    		int minSideLength, int maxNumOfPixels) 
    {
 
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
 
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize)
            	roundedSize <<= 1;
        }
        else
            roundedSize = (initialSize + 7) / 8 * 8;
        
        return roundedSize;
    }
 
    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
 
        double w = options.outWidth;
        double h = options.outHeight;
 
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
 
        if (upperBound < lowerBound) 
            // return the larger one when there is no overlapping zone.
            return lowerBound;
 
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) 
            return 1;
        
        else if (minSideLength == -1) 
            return lowerBound;
        
        else 
            return upperBound;
    }
}
