package com.smk.iwomen;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.nullwire.trace.ExceptionHandler;
import com.path.android.jobqueue.JobManager;
import com.smk.application.DownloadManager;
import com.smk.clientapi.NetworkEngine;
import com.smk.model.APKVersion;
import com.smk.model.Download;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.manager.MainApplication;

import java.io.File;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BaseActionBarActivity extends AppCompatActivity{
	private NotificationManager mNotifyManager;
	private Builder mBuilder;
	private static boolean isCheckedVersion = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExceptionHandler.register(this, "http://128.199.70.154/api-v1/error_report");
		// TODO it is for non application market.

		checkAPKVersion();

	}
	
	private void checkAPKVersion(){
		
		NetworkEngine.getInstance().getAPKVersion("", new Callback<APKVersion>() {
			
			@Override
			public void success(final APKVersion arg0, Response arg1) {
				// TODO Auto-generated method stub
				try {
					isCheckedVersion = true;
					int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

					if(arg0 != null) {

						if (arg0.getVersionId() > versionCode) {
							//TODO
							try {
								//Dialog True 4
								//Dialog False dismisss ---
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BaseActionBarActivity.this);

								// set title
								alertDialogBuilder.setTitle("New Version "+arg0.getVersionName());

								// set dialog message
								alertDialogBuilder
										.setMessage("Do you download new version?")
										.setCancelable(true)
										.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												EventBus.getDefault().register(BaseActionBarActivity.this);
												JobManager jobManager = MainApplication.getInstance().getJobManager();
												DownloadManager downloadManager = new DownloadManager("http://128.199.70.154/apk/" + arg0.getName(), arg0.getName());
												jobManager.addJob(downloadManager);
											}
										})
										.setNegativeButton("No",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {
												// if this button is clicked, just close
												// the dialog box and do nothing
												dialog.cancel();
											}
										});

								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();

								// show it
								alertDialog.show();
							}catch (WindowManager.BadTokenException e){

							}



						}
					}
				} catch (NameNotFoundException e) {

				}catch (NullPointerException ex){
					//ex.printStackTrace();
				}
				
			}
			
			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				
			}
		});		
	}
	
	// This is processing when downloading file.
	public void onEventMainThread(final Download download) {
		// the posted event can be processed in the main thread
		Log.i("", "Hello downloading precent is : " + download.getDownloadPercent());
		int id = 1;
		mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new Builder(this);
		mBuilder.setContentTitle("IWomen Update Download")
		    .setContentText("Download in progress")
		    .setSmallIcon(R.drawable.ic_launcher);
		if(download.getStatus()){
            mBuilder.setContentText("Download is complete")
            	.setProgress(0,0,false);
            mNotifyManager.notify(id, mBuilder.build());
            
            // raise notification
    		Intent intent = new Intent(Intent.ACTION_VIEW);
    		intent.setDataAndType(Uri.fromFile(new File(download.getFilePath())), "application/vnd.android.package-archive");
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
    		
		}else{
			//TODO show progress
			mBuilder.setProgress(100, download.getDownloadPercent(), false);
			mNotifyManager.notify(id, mBuilder.build());
			
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
