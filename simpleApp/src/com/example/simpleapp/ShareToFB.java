package com.example.simpleapp;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

public class ShareToFB {
	
	private static final String PERMISSION = "publish_actions";
	public final String PENDING_ACTION_BUNDLE_KEY = "com.example.simpleApp:gallery.GridView";

    public PendingAction pendingAction = PendingAction.NONE;
    private boolean canPresentShareDialogWithPhotos;
    private UiLifecycleHelper uiHelper;
    private Activity activity;
    private Context context;
    private ArrayList<File> imageFiles;
        
    public ShareToFB(Activity activity, Context context, UiLifecycleHelper uiHelper,
    		boolean canPresentShareDialogWithPhotos)
    {
    	this.activity = activity;
    	this.context = context;
    	this.uiHelper = uiHelper;
    	this.canPresentShareDialogWithPhotos = canPresentShareDialogWithPhotos;
    	imageFiles = new ArrayList<File>();
    }
    
    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }
    
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
    	for (String pathString : imagePathArrayList)
    	{
    		File file = new File(pathString);
    		imageFiles.add(file);
    		Log.e("path", file.getAbsolutePath());
    	}
        performPublish(PendingAction.POST_PHOTO, canPresentShareDialogWithPhotos);
    }
	
	private boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }
	
	private void performPublish(PendingAction action, boolean allowNoSession) {
        Session session = Session.getActiveSession();
        if (session != null) {
            pendingAction = action;
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
            pendingAction = action;
            postPhoto();
        }
    }
		
	private void postPhoto() {
        if (canPresentShareDialogWithPhotos) {
        	FacebookDialog shareDialog = new FacebookDialog.PhotoShareDialogBuilder(activity).addPhotoFiles(imageFiles).build();//createShareDialogBuilderForPhoto(image).build();

            Log.e("1", shareDialog.toString());
            uiHelper.trackPendingDialogCall(shareDialog.present());
            Log.e("1", "end");
        } /*else if (hasPublishPermission()) {
            Request request;
			try {
				request = Request.newUploadPhotoRequest(Session.getActiveSession(), file, new Request.Callback() {
				    @Override
				    public void onCompleted(Response response) {
				        showPublishResult(context.getString(R.string.photo_post), response.getGraphObject(), response.getError());
				    }
				});
	            request.executeAsync();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        } else {
            pendingAction = PendingAction.POST_PHOTO;
        }*/
    }
	
	public void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != PendingAction.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException)) {
                new AlertDialog.Builder(context)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = PendingAction.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
        	postPhoto();
        }
    }

}
