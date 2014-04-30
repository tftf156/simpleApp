package com.example.simpleapp;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

public class ShareToFB {
	
	private static final String PERMISSION = "publish_actions";
	public final String PENDING_ACTION_BUNDLE_KEY = "com.example.simpleApp:gallery.GridView";

    //public PendingAction pendingAction = PendingAction.NONE;
    private boolean canPresentShareDialogWithPhotos;
    private UiLifecycleHelper uiHelper;
    private Activity activity;
    private ArrayList<File> imageFiles;
        
    public ShareToFB(Activity activity, UiLifecycleHelper uiHelper,
    		boolean canPresentShareDialogWithPhotos)
    {
    	this.activity = activity;
    	this.uiHelper = uiHelper;
    	this.canPresentShareDialogWithPhotos = canPresentShareDialogWithPhotos;
    	imageFiles = new ArrayList<File>();
    }
    
    /*private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }*/
    
    public FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };
	
    public void onClickPostPhoto(ArrayList<String> imagePathArrayList) {
    	imageFiles.clear();
    	for (String pathString : imagePathArrayList)
    	{
    		File file = new File(pathString);
    		imageFiles.add(file);
    	}
        performPublish(canPresentShareDialogWithPhotos);
    }
	
	private boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }
	
	private void performPublish(boolean allowNoSession) {
        Session session = Session.getActiveSession();
        if (session != null) {
            
            if (hasPublishPermission()) {
                // We can do the action right away.
            	postPhoto();
                return;
            } else if (session.isOpened()) {
                // We need to get new permissions, then complete the action when we get called back.
                session.requestNewPublishPermissions(new Session.NewPermissionsRequest(activity, PERMISSION));
                return;
            }
        }

        if (allowNoSession) {
            postPhoto();
        }
    }
		
	private void postPhoto() {
        if (canPresentShareDialogWithPhotos) {
        	FacebookDialog shareDialog = new FacebookDialog.PhotoShareDialogBuilder(activity).addPhotoFiles(imageFiles).build();
        	
            uiHelper.trackPendingDialogCall(shareDialog.present());
        }
    }
}
