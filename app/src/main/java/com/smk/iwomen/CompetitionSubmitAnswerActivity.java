package com.smk.iwomen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smk.application.StoreUtil;
import com.smk.clientapi.NetworkEngine;
import com.smk.model.CompetitionQuestion;
import com.smk.model.GroupUser;
import com.smk.skalertmessage.SKToastMessage;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CompetitionSubmitAnswerActivity extends ActionBarActivity {

	private TextView txt_question;
	private TextView txt_description;
	private EditText edt_answer_1;
	private EditText edt_answer_2;
	private EditText edt_answer_3;
	private GroupUser groupUser;
	private CompetitionQuestion competitionQuestion;
	private Button btn_save;
	private Button btn_submit;
	private Integer answer3Id;
	private Integer answer2Id;
	private Integer answer1Id;

	private SharedPreferences mSharedPreferencesUserInfo;
	private SharedPreferences.Editor mEditorUserInfo;
	private String user_name, user_obj_id, user_ph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competition_submit_answer);

		mSharedPreferencesUserInfo = getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);

		user_obj_id = mSharedPreferencesUserInfo.getString(CommonConfig.USER_OBJ_ID, null);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			competitionQuestion = new Gson().fromJson(bundle.getString("competition_question"), CompetitionQuestion.class);
			groupUser = new Gson().fromJson(bundle.getString("group_user"), GroupUser.class);
		}
		
		txt_question = (TextView) findViewById(R.id.txt_competition_question);
		txt_description = (TextView) findViewById(R.id.txt_competition_description);
		
		txt_question.setText(competitionQuestion.getQuestion());
		txt_description.setText(competitionQuestion.getDescription());
		
		edt_answer_1 = (EditText) findViewById(R.id.edt_competition_answer_1);
		edt_answer_2 = (EditText) findViewById(R.id.edt_competition_answer_2);
		edt_answer_3 = (EditText) findViewById(R.id.edt_competition_answer_3);
		
		btn_save = (Button) findViewById(R.id.btn_competition_answer_save);
		btn_submit = (Button) findViewById(R.id.btn_competition_answer_submit);
		
		
		String answer1 = StoreUtil.getInstance().selectFrom("answer1_" +user_obj_id);
		if(answer1 != null){
			edt_answer_1.setText(answer1);
		}
		String answer2 = StoreUtil.getInstance().selectFrom("answer2_" +user_obj_id);
		if(answer2 != null){
			edt_answer_2.setText(answer2);
		}
		String answer3 = StoreUtil.getInstance().selectFrom("answer3_" +user_obj_id);
		if(answer3 != null){
			edt_answer_3.setText(answer3);
		}
		
		if(groupUser.getAnswer().size() >= 1){
			answer1Id = groupUser.getAnswer().get(0).getId();
			SharedPreferences langRef = getSharedPreferences("mLanguage", MODE_PRIVATE); 
			if(langRef.getString("lang","").equals("mm")){
				edt_answer_1.setText(groupUser.getAnswer().get(0).getAnswerMm());		
			}else{
				edt_answer_1.setText(groupUser.getAnswer().get(0).getAnswer());
			}
			edt_answer_1.setEnabled(false);
		}
		if(groupUser.getAnswer().size() >= 2){
			answer2Id = groupUser.getAnswer().get(1).getId();
			SharedPreferences langRef = getSharedPreferences("mLanguage", MODE_PRIVATE); 
			if(langRef.getString("lang","").equals("mm")){
				edt_answer_2.setText(groupUser.getAnswer().get(1).getAnswerMm());		
			}else{
				edt_answer_2.setText(groupUser.getAnswer().get(1).getAnswer());
			}
			edt_answer_2.setEnabled(false);
		}
		if(groupUser.getAnswer().size() >= 3){
			answer3Id = groupUser.getAnswer().get(2).getId();
			SharedPreferences langRef = getSharedPreferences("mLanguage", MODE_PRIVATE); 
			if(langRef.getString("lang","").equals("mm")){
				edt_answer_3.setText(groupUser.getAnswer().get(2).getAnswerMm());		
			}else{
				edt_answer_3.setText(groupUser.getAnswer().get(2).getAnswer());
			}
			edt_answer_3.setEnabled(false);
			btn_save.setEnabled(false);
			btn_submit.setEnabled(false);
		}
		
		btn_save.setOnClickListener(clickListener);
		btn_submit.setOnClickListener(clickListener);
	}
	
	private View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0 == btn_save){
				if(edt_answer_1.getText().toString().trim().length() > 0){
					StoreUtil.getInstance().saveTo("answer1_" +user_obj_id, edt_answer_1.getText().toString());
				}
				if(edt_answer_2.getText().toString().trim().length() > 0){
					StoreUtil.getInstance().saveTo("answer2_" +user_obj_id, edt_answer_2.getText().toString());
				}
				if(edt_answer_3.getText().toString().trim().length() > 0){
					StoreUtil.getInstance().saveTo("answer3_" +user_obj_id, edt_answer_3.getText().toString());
				}
				SKToastMessage.showMessage(CompetitionSubmitAnswerActivity.this, "Successfully saved", SKToastMessage.SUCCESS);
			}
			
			if(arg0 == btn_submit){
				postCompetitionAnswer();
			}
		}
	};
	
	private void postCompetitionAnswer(){
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...");
		dialog.show();
		NetworkEngine.getInstance().postCompetitionAnswer(
				"",
				answer1Id, 
				edt_answer_1.getText().toString(), 
				edt_answer_1.getText().toString(), 
				answer2Id, 
				edt_answer_2.getText().toString(), 
				edt_answer_2.getText().toString(), 
				answer3Id, 
				edt_answer_3.getText().toString(), 
				edt_answer_3.getText().toString(), 
				competitionQuestion.getId(), 
				groupUser.getId(), 
				new Callback<String>() {
					
					@Override
					public void success(String arg0, Response arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						SKToastMessage.showMessage(CompetitionSubmitAnswerActivity.this, arg0, SKToastMessage.SUCCESS);
						Intent returnIntent = new Intent();
						//returnIntent.putExtra("result",result);
						setResult(RESULT_OK,returnIntent);
						finish();
					}
					
					@Override
					public void failure(RetrofitError arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if(arg0.getResponse() != null){
							switch (arg0.getResponse().getStatus()) {
							case 400:
								String error = (String) arg0.getBodyAs(String.class);
								SKToastMessage.showMessage(CompetitionSubmitAnswerActivity.this, error, SKToastMessage.ERROR);
								break;

							default:
								break;
							}
						}
					}
				});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
		super.onBackPressed();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.competition_submit_answer, menu);
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
}
