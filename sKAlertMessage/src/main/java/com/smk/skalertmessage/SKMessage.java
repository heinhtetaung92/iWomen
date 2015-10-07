package com.smk.skalertmessage;


import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SKMessage {
	private static SKMessage instance;
	private String Message;
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int INFO = 2;
	public static final int WARNING = 3;
	private int MessageType;
	private View SuccessView;
	private View ErrorView;
	private View InfoView;
	private View WarningView;
	private Activity activity;
	private TextView txt_message;
	private View btn_delete;
	private LayoutParams params;
	public SKMessage(Activity activity) {
		this.activity = activity;
		// TODO Auto-generated constructor stub
	}
	
	public static SKMessage getInstance(Activity activity) {
    	if(instance == null){
    		instance = new SKMessage(activity);
		}
    	instance.init();
		return instance;
	}
	
	public static void showMessage(Activity activity, String message, int messageType){
		if(instance == null){
			instance = new SKMessage(activity);
		}
		instance.init();
		instance.setMessage(message);
		instance.setMessageType(messageType);
		instance.show();
	}
	
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public int getMessageType() {
		return MessageType;
	}
	public void setMessageType(int messageType) {
		MessageType = messageType;
	}
	
	public void show(){
		switch (getMessageType()) {
		case SUCCESS:
			showSuccess();
			break;
		case ERROR:
			showError();
			break;
		case INFO:
			showInfo();
			break;
		case WARNING:
			showWarning();
			break;
		default:
			showSuccess();
			break;
		}
		if(getMessage() == null){
			throw new RuntimeException("Message may not empty, Please use setMessage(...).");
		}
		
	}
	
	private void init(){
		
		SuccessView = View.inflate(activity, R.layout.sk_message_success, null);
		ErrorView = View.inflate(activity, R.layout.sk_message_error, null);
		InfoView = View.inflate(activity, R.layout.sk_message_info, null);
		WarningView = View.inflate(activity, R.layout.sk_message_warning, null);
		
		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		activity.addContentView(SuccessView, params);
		activity.addContentView(ErrorView, params);
		activity.addContentView(InfoView, params);
		activity.addContentView(WarningView, params);
		
		SuccessView.setVisibility(View.GONE);
		ErrorView.setVisibility(View.GONE);
		InfoView.setVisibility(View.GONE);
		WarningView.setVisibility(View.GONE);
		
	}
	
	private void showSuccess(){
		getSuccessView().setVisibility(View.VISIBLE);
		getErrorView().setVisibility(View.GONE);
		getInfoView().setVisibility(View.GONE);
		getWarningView().setVisibility(View.GONE);	
		
		getSuccessView().startAnimation(AnimationUtils.loadAnimation(activity, R.anim.top_in));
		
		txt_message = (TextView) getSuccessView().findViewById(R.id.sk_txt_message);
		txt_message.setText(getMessage());
		
		btn_delete = getSuccessView().findViewById(R.id.sk_btn_delete);
		btn_delete.setOnClickListener(new OnClickListener() {
			
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				Log.v("SKMessage","SKMessage is close.");
				getSuccessView().startAnimation(AnimationUtils.loadAnimation(activity, R.anim.top_out));
				getSuccessView().postDelayed(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						getSuccessView().setVisibility(View.GONE);
					}
				}, 1000);
			}
		});
	}
	
	private void showError(){
		getSuccessView().setVisibility(View.GONE);
		getErrorView().setVisibility(View.VISIBLE);
		getInfoView().setVisibility(View.GONE);
		getWarningView().setVisibility(View.GONE);	
		
		getErrorView().startAnimation(AnimationUtils.loadAnimation(activity, R.anim.top_in));
		
		txt_message = (TextView) getErrorView().findViewById(R.id.sk_txt_message);
		txt_message.setText(getMessage());
		
		btn_delete = getErrorView().findViewById(R.id.sk_btn_delete);
		btn_delete.setOnClickListener(new OnClickListener() {
			
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				Log.v("SKMessage","SKMessage is close.");
				getErrorView().startAnimation(AnimationUtils.loadAnimation(activity, R.anim.top_out));
				getErrorView().postDelayed(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						getErrorView().setVisibility(View.GONE);
					}
				}, 1000);
			}
		});
	}
	
	private void showInfo(){
		getSuccessView().setVisibility(View.GONE);
		getErrorView().setVisibility(View.GONE);
		getInfoView().setVisibility(View.VISIBLE);
		getWarningView().setVisibility(View.GONE);	
		
		getInfoView().startAnimation(AnimationUtils.loadAnimation(activity, R.anim.top_in));
		
		txt_message = (TextView) getInfoView().findViewById(R.id.sk_txt_message);
		txt_message.setText(getMessage());
		
		btn_delete = getInfoView().findViewById(R.id.sk_btn_delete);
		btn_delete.setOnClickListener(new OnClickListener() {
			
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				Log.v("SKMessage","SKMessage is close.");
				getInfoView().startAnimation(AnimationUtils.loadAnimation(activity, R.anim.top_out));
				getInfoView().postDelayed(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						getInfoView().setVisibility(View.GONE);
					}
				}, 1000);
			}
		});
	}
	
	private void showWarning(){
		getSuccessView().setVisibility(View.GONE);
		getErrorView().setVisibility(View.GONE);
		getInfoView().setVisibility(View.GONE);
		getWarningView().setVisibility(View.VISIBLE);
		
		getWarningView().startAnimation(AnimationUtils.loadAnimation(activity, R.anim.top_in));
		
		txt_message = (TextView) getWarningView().findViewById(R.id.sk_txt_message);
		txt_message.setText(getMessage());
		
		btn_delete = getWarningView().findViewById(R.id.sk_btn_delete);
		btn_delete.setOnClickListener(new OnClickListener() {
			
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				Log.v("SKMessage","SKMessage is close.");
				getWarningView().startAnimation(AnimationUtils.loadAnimation(activity, R.anim.top_out));
				getWarningView().postDelayed(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						getWarningView().setVisibility(View.GONE);
					}
				}, 1000);
			}
		});
	}
	
	public View getSuccessView() {
		return SuccessView;
	}

	public void setSuccessView(View successView) {
		SuccessView = successView;
	}

	public View getErrorView() {
		return ErrorView;
	}

	public void setErrorView(View errorView) {
		ErrorView = errorView;
	}

	public View getInfoView() {
		return InfoView;
	}

	public void setInfoView(View infoView) {
		InfoView = infoView;
	}

	public View getWarningView() {
		return WarningView;
	}

	public void setWarningView(View warningView) {
		WarningView = warningView;
	}

	
}
