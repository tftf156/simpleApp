package com.example.gallery;

import java.util.ArrayList;

import com.example.camera.FilterActivity;
import com.example.gallery.GridView.GridViewActivity;
import com.example.gallery.GridView.GridViewActivity.PendingAction;
import com.example.simpleapp.R;
import com.example.simpleapp.ShareToFB;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class ChopPhotoActivity extends ActionBarActivity {
	private String ImgPath;
	private ImageView ImgView;
	Button shareButton;
	Button filterButton;
	private ShareToFB shareToFB;
	private UiLifecycleHelper uiHelper;
	private PendingAction pendingAction = PendingAction.NONE;
	private boolean canPresentShareDialogWithPhotos;
	final int PIC_CROP = 1;
	public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;
	private Spinner RatioSpinner;
	private String array_spinner[];
	int aspectX;
	int aspectY;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chop_photo);
		uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			ImgPath = extras.getString("image path");
			Log.d("123", ImgPath);
		}
		
		ImgView = (ImageView)findViewById(R.id.chopPhotoImageView);
		ImgView.setImageURI(Uri.parse("file://mnt" + ImgPath));
		
		array_spinner=new String[4];
        array_spinner[0]="1:1";
        array_spinner[1]="4:3";
        array_spinner[2]="16:9";
        array_spinner[3]="16:10";
        RatioSpinner = (Spinner) findViewById(R.id.ratioSpinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);
        RatioSpinner.setAdapter(adapter);
		
        Button cropButton = (Button)findViewById(R.id.GoCropButton);
        cropButton.setOnClickListener(cropButtonOnClicked);
        
        
        canPresentShareDialogWithPhotos = FacebookDialog.canPresentShareDialog(this,
                FacebookDialog.ShareDialogFeature.PHOTOS);
        shareToFB = new ShareToFB(ChopPhotoActivity.this, uiHelper, canPresentShareDialogWithPhotos);
        
        shareButton = (Button)findViewById(R.id.ShareButton);
        shareButton.setOnClickListener(shareButtonOnClicked);
        
        filterButton = (Button)findViewById(R.id.applyFilterButton);
        filterButton.setOnClickListener(filterButtonOnClicked);
		
		//performCrop(Uri.parse("file://mnt" + ImgPath));

		//if (savedInstanceState == null) {
		//	getSupportFragmentManager().beginTransaction()
		//			.add(R.id.container, new PlaceholderFragment()).commit();
		//}
	}
	
	OnClickListener cropButtonOnClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(RatioSpinner.getSelectedItemPosition() == 0)
			{
				aspectX = 1;
				aspectY = 1;
			}
			else if(RatioSpinner.getSelectedItemPosition() == 1)
			{
				aspectX = 4;
				aspectY = 3;
			}
			else if(RatioSpinner.getSelectedItemPosition() == 2)
			{
				aspectX = 16;
				aspectY = 9;
			}
			else if(RatioSpinner.getSelectedItemPosition() == 3)
			{
				aspectX = 16;
				aspectY = 10;
			}
			
			performCrop(Uri.parse("file://mnt" + ImgPath));
		}
	};
	
	OnClickListener shareButtonOnClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ArrayList<String> arraylist = new ArrayList<String>();
			arraylist.add(ImgPath);
			shareToFB.onClickPostPhoto(arraylist);
		}
	};
	
	OnClickListener filterButtonOnClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(ChopPhotoActivity.this, FilterActivity.class);
			intent.putExtra("image path", ImgPath);
			startActivity(intent);
		}
	};
	
	private void performCrop(Uri picUri) {
		Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, ImgPath);
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, aspectX);
        intent.putExtra(CropImage.ASPECT_Y, aspectY);
        //intent.putExtra(CropImage.OUTPUT_X, 1000);
        //intent.putExtra(CropImage.OUTPUT_Y, 1000);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    Log.d("123","123123");
	    
	    Bitmap bitmap;

        switch (requestCode) {
            case REQUEST_CODE_CROP_IMAGE:

                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                Log.d("123path", path);
                if (path == null) {
                	Log.d("123path", "NULL");
                    return;
                }

                //bitmap = BitmapFactory.decodeFile(path);
                ImgView.setImageURI(Uri.parse(path));
                //ImgView.setImageURI(Uri.parse("file://mnt" + ImgPath));
                break;
        }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chop_photo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_chop_photo,
					container, false);
			return rootView;
		}
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != PendingAction.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException)) {
                new AlertDialog.Builder(ChopPhotoActivity.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = PendingAction.NONE;
        } 
    }

}
