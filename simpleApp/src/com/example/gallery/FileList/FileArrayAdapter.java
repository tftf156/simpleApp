package com.example.gallery.FileList;

import java.util.List;

import com.example.simpleapp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class FileArrayAdapter extends ArrayAdapter<Albumb> {

	private Context c;
	private int id;
	private List<Albumb> items;

	public FileArrayAdapter(Context context, int textViewResourceId,
			List<Albumb> objects) {
		super(context, textViewResourceId, objects);
		c = context;
		id = textViewResourceId;
		items = objects;
	}

	public Albumb getItem(int i) {
		return items.get(i);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(id, null);
		}

		/* create a new view of my layout and inflate it in the row */
		// convertView = ( RelativeLayout ) inflater.inflate( resource, null );

		final Albumb item = items.get(position);
		if (item != null) {
			TextView t1 = (TextView) v.findViewById(R.id.nameTextView);
			TextView t2 = (TextView) v.findViewById(R.id.TextView02);
			TextView t3 = (TextView) v.findViewById(R.id.TextViewDate);
			/* Take the ImageView from layout and set the city's image */
			ImageView imageCity = (ImageView) v.findViewById(R.id.fd_Icon1);

			String type = item.getImage();
			if (type.equalsIgnoreCase("directory_icon")) {
				String uri = "drawable/" + item.getImage();
				int imageResource = c.getResources().getIdentifier(uri, null,
						c.getPackageName());
				Drawable image = c.getResources().getDrawable(imageResource);
				imageCity.setImageDrawable(image);
			} else {
				Bitmap bmp = BitmapFactory.decodeFile(item.getPath());
				if(bmp == null) {
					String uri = "drawable/" + item.getImage();
					int imageResource = c.getResources().getIdentifier(uri, null,
							c.getPackageName());
					Drawable image = c.getResources().getDrawable(imageResource);
					imageCity.setImageDrawable(image);
				} else {
					Bitmap tempBitmap = Bitmap.createScaledBitmap(bmp, 34, 60, true);
					imageCity.setImageBitmap(tempBitmap);
				}
			}
			if (t1 != null)
				t1.setText(item.getName());
			if (t2 != null)
				t2.setText(item.getData());
			if (t3 != null)
				t3.setText(item.getDate());

		}
		return v;
	}

}
