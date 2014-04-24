package com.example.gallery.GridView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.simpleapp.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

public class GridViewActivity extends Activity{

	private static int VISIBLE = 0;
	private static int INVISIBLE = 4;
	
	private GridView gridView;
	private GridViewAdapter customGridAdapter;
	private Intent intent;
	private String path;
	private File targetFile;
	private DisplayMetrics displayMetrics;
	private Integer phoneWidth, phoneHeight;
	private RelativeLayout functions;
	private FrameLayout deleteFrameLayout, 
		shareFrameLayout, chopFrameLayout;
	private DeleteAlertDialog deleteAlertDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview);

		intent = this.getIntent();
		path = intent.getStringExtra("path");
		targetFile = new File(path);
		/*displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		phoneWidth = displayMetrics.widthPixels;
		phoneHeight = displayMetrics.heightPixels;*/
		
		gridView = (GridView) findViewById(R.id.gridView);
		customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, getData());
		gridView.setAdapter(customGridAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				customGridAdapter.changeState(position, gridView);
				if(customGridAdapter.isItemSelected())
					functions.setVisibility(VISIBLE);
				else
					functions.setVisibility(INVISIBLE);
			}
			
		});
				
		functions = (RelativeLayout) findViewById(R.id.functions);
		functions.setVisibility(INVISIBLE);
		
		deleteAlertDialog = new DeleteAlertDialog(this, customGridAdapter, gridView);
		
		deleteFrameLayout = (FrameLayout) findViewById(R.id.deleteFrameLayout);
		deleteFrameLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteAlertDialog.onShow(functions);
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
			imageItems.add(new ImageItem(bitmap, file.getName(), file.getAbsolutePath()));
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
