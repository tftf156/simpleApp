package com.example.gallery.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import com.example.simpleapp.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends ArrayAdapter<ImageItem>{

	private Context context;
	private int layoutResourceId;
	private ArrayList<ImageItem> data = new ArrayList<ImageItem>();
	private Vector<Boolean> imageSelect = new Vector<Boolean>();

	public GridViewAdapter(Context context, int layoutResourceId,
			ArrayList<ImageItem> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		for(int i = 0 ; i < data.size(); i++)
			imageSelect.add(false);
	}

	@Override 
    public int getCount() {    
            // TODO Auto-generated method stub    
            return data.size();    
    }    
	
	@Override
	public ImageItem getItem(int position)
	{
		return data.get(position);
	}
 
    @Override 
    public long getItemId(int position) {    
            // TODO Auto-generated method stub    
             return position;    
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.imageTitle = (TextView) row.findViewById(R.id.text);
			holder.image = (ImageView) row.findViewById(R.id.image);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		ImageItem item = data.get(position);
		holder.imageTitle.setText(item.getTitle());
		holder.image.setImageDrawable(makeBmp(position, imageSelect.elementAt(position)));
		Integer integer = position;
		Log.e(item.getTitle(), integer.toString());
		//holder.image.setImageBitmap(item.getImage());
		return row;
	}
	
	private LayerDrawable makeBmp(int id, boolean isChosen)
	{
		int width = data.get(id).getImage().getWidth() + 20;
		int height = data.get(id).getImage().getHeight();
		Bitmap spaceBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        // 根据isChosen来选取对勾的图片    
        Bitmap seletedBmp;

        
        if(isChosen == true)
        {
        	Drawable[] array = new Drawable[2];
        	seletedBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.yes);
        	array[0] = new BitmapDrawable(data.get(id).getImage());    
            array[1] = new BitmapDrawable(seletedBmp);    
            LayerDrawable la = new LayerDrawable(array);    
            la.setLayerInset(0, 0, 0, 0, 0);    
            la.setLayerInset(1, 0, 0, 0, 0);
            
            return la;
        }
        else
        {
        	Drawable[] array = new Drawable[1];
        	array[0] = new BitmapDrawable(data.get(id).getImage());    
            LayerDrawable la = new LayerDrawable(array);    
            la.setLayerInset(0, 0, 0, 0, 0);
            
            return la;
        }
	}
	
	public void changeState(int position, GridView gridView)
	{    
		imageSelect.setElementAt(!imageSelect.elementAt(position), position);
		//changeColor(gridView);
		
		notifyDataSetChanged();
	}
	
	public boolean isItemSelected()
	{
		for (Boolean boolean1 : imageSelect)
			if(boolean1)
				return true;
		
		return false;
	}
	
	public void deleteAllSelectedItems(GridView gridView)
	{
		for(int i = 0; i < imageSelect.size(); i++)
		{
			if(imageSelect.elementAt(i))
			{
				File file = new File(data.get(i).getPath());
				file.delete();
				data.remove(i);
				imageSelect.remove(i);
				i--;
			}
		}
		//changeColor(gridView);
		
		notifyDataSetChanged();
	}
	
	public ArrayList<String> getSelectedImagePath()
	{
		ArrayList<String> resultArrayList = new ArrayList<>();
		for(int i = 0; i < data.size(); i++)
		{
			if(imageSelect.elementAt(i))
			{
				resultArrayList.add(data.get(i).getPath());
			}
		}
		return resultArrayList;
	}
	
	public String getSelectedSingleImagePath()
	{
		String ImgPath = new String();
		for(int i = 0; i < data.size(); i++)
		{
			if(imageSelect.elementAt(i))
			{
				ImgPath = data.get(i).getPath();
			}
		}
		return ImgPath;
	}

	static class ViewHolder {
		TextView imageTitle;
		ImageView image;
	}
}
