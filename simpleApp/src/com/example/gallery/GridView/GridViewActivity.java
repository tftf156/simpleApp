package com.example.gallery.GridView;

import java.io.File;
import java.util.ArrayList;
import com.example.simpleapp.R;
import com.example.simpleapp.ShareToFB;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
    //private PendingAction pendingAction = PendingAction.NONE;
    private boolean canPresentShareDialogWithPhotos;
    private UiLifecycleHelper uiHelper;
    private ShareToFB shareToFB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview);
		uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        

		intent = this.getIntent();
		path = intent.getStringExtra("path");
		targetFile = new File(path);
		
		gridView = (GridView) findViewById(R.id.gridView);
		customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, getData());
		gridView.setAdapter(customGridAdapter);
		gridView.setOnItemClickListener(clickGridViewClickListener);
				
		functions = (RelativeLayout) findViewById(R.id.functions);
		functions.setVisibility(INVISIBLE);
		
		deleteAlertDialog = new DeleteAlertDialog(this, customGridAdapter, gridView);
		
		deleteFrameLayout = (FrameLayout) findViewById(R.id.deleteFrameLayout);
		deleteFrameLayout.setOnClickListener(deleteClickListener);
		
		// Can we present the share dialog for photos?
        canPresentShareDialogWithPhotos = FacebookDialog.canPresentShareDialog(this,
                FacebookDialog.ShareDialogFeature.PHOTOS);
        shareToFB = new ShareToFB(GridViewActivity.this, this, uiHelper, canPresentShareDialogWithPhotos);
		shareFrameLayout = (FrameLayout) findViewById(R.id.shareFrameLayout);
		shareFrameLayout.setOnClickListener(shareClickListener);
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

        //outState.putString(PENDING_ACTION_BUNDLE_KEY, shareToFB.pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, shareToFB.dialogCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            shareToFB.onSessionStateChange(session, state, exception);
        }
    };

	OnItemClickListener clickGridViewClickListener = new OnItemClickListener() {
		
		public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
			customGridAdapter.changeState(position, gridView);
			if(customGridAdapter.isItemSelected())
				functions.setVisibility(VISIBLE);
			else
				functions.setVisibility(INVISIBLE);
		}
		
	};
	
	OnClickListener deleteClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			deleteAlertDialog.onShow(functions);
		}
		
	};
	
	OnClickListener shareClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			shareToFB.onClickPostPhoto(customGridAdapter.getSelectedImagePath());
		}
	};
	
	OnClickListener chopClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
		}
	};

	private ArrayList<ImageItem> getData() 
	{
		final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
		File[] dirs = targetFile.listFiles();
		String[] spaceStrings;
		ShrinkImage shrinkImage = new ShrinkImage(this);
		
		for (File file : dirs)
		{
			spaceStrings = file.getName().split("\\.");
			if(spaceStrings[spaceStrings.length - 1].equals("jpg"))
			{
				Bitmap bitmap = shrinkImage.getBitmap(Uri.fromFile(file));
				imageItems.add(new ImageItem(bitmap, file.getName(), file.getAbsolutePath()));
			}
		}
		
		return imageItems;
	}
}
