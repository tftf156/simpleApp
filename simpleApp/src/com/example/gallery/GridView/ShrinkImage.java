package com.example.gallery.GridView;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class ShrinkImage {

	private Context context;
	
	public ShrinkImage(Context context)
	{
		this.context = context;
	}
	
	public Bitmap getBitmap(Uri uri) 
	{
        try {
            InputStream in = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, opts);
            in.close();
 
            int sampleSize = computeSampleSize(opts, -1, 128 * 128);
 
            in = context.getContentResolver().openInputStream(uri);
            opts = new BitmapFactory.Options();
            opts.inSampleSize = sampleSize;
 
            Bitmap bmp = BitmapFactory.decodeStream(in, null, opts);
            in.close();
            Integer widthInteger = bmp.getWidth();
            Integer heightInteger = bmp.getHeight();
            Log.e("w * h", widthInteger+"*"+heightInteger);
 
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
