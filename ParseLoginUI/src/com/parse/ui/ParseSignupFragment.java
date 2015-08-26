/*
 *  Copyright (c) 2014, Parse, LLC. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Parse.
 *
 *  As with any software that integrates with the Parse platform, your use of
 *  this software is subject to the Parse Terms of Service
 *  [https://www.parse.com/about/terms]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.parse.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.makeramen.RoundedImageView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.model.MyTypeFace;
import com.parse.utils.Utils;
import com.parse.utils.ValidatorUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fragment for the user signup screen.
 */
public class ParseSignupFragment extends ParseLoginFragmentBase implements OnClickListener {

    /**
     * *******My Update Code***********
     */
    private static final String TAG = "ParseSignupFragment";

    public static final String USERNAME = "com.parse.ui.ParseSignupFragment.USERNAME";
    public static final String PASSWORD = "com.parse.ui.ParseSignupFragment.PASSWORD";

    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    //private EditText emailField;

    //private EditText addressField;
    private EditText mobileNoForNrcField;
    /*private EditText mobileNoForPassportField;
    private EditText nrcNoField;
    private EditText passportFiled;*/
    private Button createAccountButton;
    private Button editAccountButton;
    private ParseOnLoginSuccessListener onLoginSuccessListener;

    private TextInputLayout mUserNameTextInputLayout;
    //private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private TextInputLayout mConfirmPasswordTextInputLayout;
    /*private TextInputLayout mNrcTextInputLayout;
    private TextInputLayout mPassportTextInputLayout;*/
    private TextInputLayout mMobileNoForNrcTextInputLayout;
    //private TextInputLayout mMobileNoForPassportTextInputLayout;

    private CheckBox mIAgreeCheckBox;
    private TextView mTosTextView;

    private ParseLoginConfig config;
    private int minPasswordLength;

    private static final String LOG_TAG = "ParseSignupFragment";
    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 6;
    private static final String USER_OBJECT_NAME_FIELD = "name";
    /**
     * *******My Update Code***********
     */
    private LoginButton btnLoginWithFacebook;
    private RoundedImageView profile_rounded;
    private ProgressBar register_profilePic_progressBar;
    private Button btnContinue;
    private CallbackManager callbackManager;
    private JSONObject user;

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String FIELDS = "fields";

    private static final String REQUEST_FIELDS =
            TextUtils.join(",", new String[]{ID, NAME, PICTURE});

    private String user_fb_id , user_fb_email;
    private Context mContext;

    private static final float MAX_TEXTURE_SIZE = 1024f;

    // variable for control doc Type
    //private RadioGroup mDocTypeRadioGroup;

    //private enum DocType {NRC, PASSPORT}

    ;
    //private DocType mDocType = DocType.NRC;
    private EditText mCountryCodeEditText;

    private TextView img_upload_textview;
    SharedPreferences sharePref;

    String lang;

    private Uri photoUri;
    private ScaleAndSetImageTask runningImageTask;
    private static final int CAMERA = 0;
    private static final int GALLERY = 1;
    private static final String PHOTO_URI_KEY = "photo_uri";
    private static final String TEMP_URI_KEY = "temp_uri";
    private static final String FILE_PREFIX = "scrumptious_img_";
    private static final String FILE_SUFFIX = ".jpg";

    private Uri tempUri = null;
    //private File imageFile;



    /**
     * *********************************
     */

    public static ParseSignupFragment newInstance(Bundle configOptions, String username, String password) {
        ParseSignupFragment signupFragment = new ParseSignupFragment();
        Bundle args = new Bundle(configOptions);
        args.putString(ParseSignupFragment.USERNAME, username);
        args.putString(ParseSignupFragment.PASSWORD, password);
        signupFragment.setArguments(args);
        return signupFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        /**********My Update Code************/
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        /**********My Update Code************/
        Bundle args = getArguments();
        config = ParseLoginConfig.fromBundle(args, getActivity());

        minPasswordLength = DEFAULT_MIN_PASSWORD_LENGTH;
        if (config.getParseSignupMinPasswordLength() != null) {
            minPasswordLength = config.getParseSignupMinPasswordLength();
        }

        //et_user_name = (EditText)v.findViewById(R.id.signup_username_input);//et_user_name_login



        mContext = getActivity().getApplicationContext();
        //addressField = (EditText) v.findViewById(R.id.signup_address_input);
        //ImageView appLogo = (ImageView) v.findViewById(R.id.app_logo);
        String username = "KHin";//(String) args.getString(USERNAME);
        String password = "12345";//(String) args.getString(PASSWORD);

        View v = inflater.inflate(R.layout.com_parse_ui_parse_signup_fragment,
                parent, false);

        usernameField = (EditText) v.findViewById(R.id.signup_username_input);

        passwordField = (EditText) v.findViewById(R.id.signup_password_input);
        confirmPasswordField = (EditText) v.findViewById(R.id.signup_confirm_password_input);
        mobileNoForNrcField = (EditText) v.findViewById(R.id.signup_mobile_number_for_nrc_input);

        /*emailField = (EditText) v.findViewById(R.id.signup_email_input);
        mobileNoForPassportField = (EditText) v.findViewById(R.id.signup_mobile_number_for_passport_input);
        nrcNoField = (EditText) v.findViewById(R.id.signup_nrc_input);
        passportFiled = (EditText) v.findViewById(R.id.signup_passport_input);*/

        mCountryCodeEditText = (EditText) v.findViewById(R.id.signup_confirm_password_input);

        /*mDocTypeRadioGroup = (RadioGroup) v.findViewById(R.id.signup_doc_type_radio_group);
        mDocTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onDocTypeChanged(checkedId);
            }
        });*/

        mUserNameTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_user_name);
        mPasswordTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_password);
        mConfirmPasswordTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_confirm_password);

        mMobileNoForNrcTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_mobile_number_for_nrc_input);

        mTosTextView = (TextView) v.findViewById(R.id.terms_and_conditions_textview);
        mTosTextView.setClickable(true);
        mTosTextView.setMovementMethod(LinkMovementMethod.getInstance());

        /*mEmailTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_email_address);
        mMobileNoForPassportTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_mobile_number_for_passport_input);
        mNrcTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_nrc_input);
        mPassportTextInputLayout = (TextInputLayout) v.findViewById(R.id.til_signup_passport_input);*/

        // mNrcTextInputLayout.setError(getResources().getString(R.string.nrc_number_helper));
        createAccountButton = (Button) v.findViewById(R.id.create_account);
        editAccountButton = (Button)v.findViewById(R.id.register_edit_account);

        if (config.getParseSignupSubmitButtonText() != null) {
            createAccountButton.setText(config.getParseSignupSubmitButtonText());
        }
        createAccountButton.setOnClickListener(this);

        mIAgreeCheckBox = (CheckBox) v.findViewById(R.id.i_agree_checkbox);
        mIAgreeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    createAccountButton.setEnabled(true);
                } else {
                    createAccountButton.setEnabled(false);
                }
            }
        });

        btnLoginWithFacebook = (LoginButton) v.findViewById(R.id.facebook_login);

        profile_rounded = (RoundedImageView)v.findViewById(R.id.register_profilePic_rounded);
        register_profilePic_progressBar = (ProgressBar)v.findViewById(R.id.register_profilePic_progressBar);
        register_profilePic_progressBar.setVisibility(View.GONE);
        profile_rounded.setAdjustViewBounds(true);
        profile_rounded.setScaleType(ImageView.ScaleType.CENTER_CROP);

        profile_rounded.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utils.doToastEng(getActivity().getApplicationContext(),"Coming soon pls !");
                showPhotoChoice();

            }
        });

        img_upload_textview = (TextView)v.findViewById(R.id.register_img_upload_textview);


        //btnLoginWithFacebook.setText("Connect with facebook");
        btnLoginWithFacebook.setReadPermissions("public_profile", "email", "user_friends");
        // If using in a fragment
        btnLoginWithFacebook.setFragment(this);
        btnLoginWithFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                //Utils.doToast(getActivity(), "Login successful Facebook");
                btnLoginWithFacebook.setVisibility(View.GONE);

                updateUI();


            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Login canceled", Toast.LENGTH_SHORT).show();
                //Utils.doToast(getActivity(), "Login canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Login error", Toast.LENGTH_SHORT).show();
                //Utils.doToast(getActivity(), "Login error");
            }
        });

        /******Font Setting*********/
        sharePref = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        lang = sharePref.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        if(lang.equals(Utils.ENG_LANG)){

            setEnglishFont();
        }
        else if(lang.equals(Utils.MM_LANG)){

            setMyanmarFont();
        }
        /******Font Setting*********/


        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ParseOnLoginSuccessListener) {
            onLoginSuccessListener = (ParseOnLoginSuccessListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoginSuccessListener");
        }

        if (activity instanceof ParseOnLoadingListener) {
            onLoadingListener = (ParseOnLoadingListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoadingListener");
        }
    }

    /*private void onDocTypeChanged(int checkId) {
        if (checkId == R.id.signup_doc_type_nrc_radio_button) {

            nrcNoField.setVisibility(View.VISIBLE);
            passportFiled.setVisibility(View.GONE);

            mNrcTextInputLayout.setErrorEnabled(false);
            mPassportTextInputLayout.setErrorEnabled(false);
            mMobileNoForPassportTextInputLayout.setErrorEnabled(false);
            mMobileNoForNrcTextInputLayout.setErrorEnabled(false);

            mobileNoForNrcField.setVisibility(View.VISIBLE);
            mobileNoForPassportField.setVisibility(View.GONE);

            mDocType = DocType.NRC;

        } else if (checkId == R.id.signup_doc_type_other_radio_button) {

            nrcNoField.setVisibility(View.GONE);
            passportFiled.setVisibility(View.VISIBLE);

            mobileNoForNrcField.setVisibility(View.GONE);
            mobileNoForPassportField.setVisibility(View.VISIBLE);

            mNrcTextInputLayout.setErrorEnabled(false);
            mPassportTextInputLayout.setErrorEnabled(false);
            mMobileNoForNrcTextInputLayout.setErrorEnabled(false);
            mMobileNoForPassportTextInputLayout.setErrorEnabled(false);

            mDocType = DocType.PASSPORT;
        }
    }*/

    @Override
    public void onClick(View v) {

        //clearError();

        //showToast(et_ph.getText().toString() + et_nrc.getText().toString()+et_address.getText().toString());
        String username = usernameField.getText().toString().trim();

        String password = passwordField.getText().toString();
        String passwordAgain = confirmPasswordField.getText().toString();
        String mobileNoForNrc = mobileNoForNrcField.getText().toString().trim();

        /*String email = emailField.getText().toString().trim();
        String mobileNoForPassport = mobileNoForPassportField.getText().toString().trim();
        String nrcNo = nrcNoField.getText().toString().trim();
        String passportNo = passportFiled.getText().toString().trim();*/

        if (TextUtils.isEmpty(username)) {
            mUserNameTextInputLayout.setError(getResources().getString(R.string.your_name_error));

            if(lang.equals(Utils.ENG_LANG)){
                doToast(getResources().getString(R.string.your_name_error));
            }
            else if(lang.equals(Utils.MM_LANG)){

                Utils.doToastMM(mContext, getResources().getString(R.string.your_name_error_mm));
            }

            return;
        } else {
            mUserNameTextInputLayout.setErrorEnabled(false);
        }

        /*if (TextUtils.isEmpty(email)) {
            mEmailTextInputLayout.setError(getResources().getString(R.string.email_address_error));
            doToast(getResources().getString(R.string.email_address_error));
            return;
        } else {
            mEmailTextInputLayout.setErrorEnabled(false);
        }*/

        /*if (TextUtils.isEmpty(password)) {
            mPasswordTextInputLayout.setError(getResources().getString(R.string.password_error));
            doToast(getResources().getString(R.string.passport_error));
            return;
        } else {
            mPasswordTextInputLayout.setErrorEnabled(false);
        }*/

        if (TextUtils.isEmpty(passwordAgain)) {
            mConfirmPasswordTextInputLayout.setError(getResources().getString(R.string.confirm_password_error));

            if(lang.equals(Utils.ENG_LANG)){
                doToast(getResources().getString(R.string.confirm_password_error));
            }
            else if(lang.equals(Utils.MM_LANG)){

                Utils.doToastMM(mContext, getResources().getString(R.string.confirm_password_error_mm));
            }
            return;
        } else {
            mConfirmPasswordTextInputLayout.setErrorEnabled(false);
        }

        if (password.length() < minPasswordLength) {

            if(lang.equals(Utils.ENG_LANG)){

                showToast(getResources().getQuantityString(
                        R.plurals.com_parse_ui_password_too_short_toast,
                        minPasswordLength, minPasswordLength));
            }
            else if(lang.equals(Utils.MM_LANG)){

                Utils.doToastMM(mContext, getResources().getQuantityString(
                        R.plurals.com_parse_ui_password_too_short_toast_mm,
                        minPasswordLength, minPasswordLength));
            }
            return;
        }

        if (!password.equals(passwordAgain)) {
            //showToast(R.string.com_parse_ui_mismatch_confirm_password_toast);
            if(lang.equals(Utils.ENG_LANG)){
                doToast(getResources().getString(R.string.com_parse_ui_mismatch_confirm_password_toast));
            }
            else if(lang.equals(Utils.MM_LANG)){

                Utils.doToastMM(mContext, getResources().getString(R.string.com_parse_ui_mismatch_confirm_password_toast_mm));
            }
            confirmPasswordField.selectAll();
            confirmPasswordField.requestFocus();
            return;
        }
        boolean inputMobileOk = true;

        if (TextUtils.isEmpty(mobileNoForNrc)) {
            mMobileNoForNrcTextInputLayout.setError(getResources().getString(R.string.mobile_number_error));
            //doToast(getResources().getString(R.string.mobile_number_error));
            if(lang.equals(Utils.ENG_LANG)){
                doToast(getResources().getString(R.string.mobile_number_error));
            }
            else if(lang.equals(Utils.MM_LANG)){

                Utils.doToastMM(mContext, getResources().getString(R.string.mobile_number_error_mm));
            }
            inputMobileOk = false;
            return;
        } else if (!ValidatorUtils.isValidMobileNo(mobileNoForNrc)) {
            mMobileNoForNrcTextInputLayout.setError(getResources().getString(R.string.invalid_mobile_number));
            //doToast(getResources().getString(R.string.invalid_mobile_number));
            if(lang.equals(Utils.ENG_LANG)){
                doToast(getResources().getString(R.string.invalid_mobile_number));
            }
            else if(lang.equals(Utils.MM_LANG)){

                Utils.doToastMM(mContext, getResources().getString(R.string.invalid_mobile_number_mm));
            }
            inputMobileOk = false;
            return;
        }
        if (inputMobileOk) {
            mMobileNoForNrcTextInputLayout.setErrorEnabled(false);
        }

        // do validation for nrc
        /*if (mDocType == DocType.NRC) {
            boolean inputNrcOk = true;
            boolean inputMobileOk = true;
            if (TextUtils.isEmpty(nrcNo)) {
                mNrcTextInputLayout.setError(getResources().getString(R.string.nrc_number_error));
                doToast(getResources().getString(R.string.nrc_number_error));
                inputNrcOk = false;
                return;
            } else if (!ValidatorUtils.isValidNrc(nrcNo)) {
                mNrcTextInputLayout.setError(getResources().getString(R.string.invalid_nrc_number_error));
                doToast(getResources().getString(R.string.invalid_nrc_number_error));
                inputNrcOk = false;
                return;
            }

            if (inputNrcOk) {
                mNrcTextInputLayout.setErrorEnabled(false);
            }

            if (TextUtils.isEmpty(mobileNoForNrc)) {
                mMobileNoForNrcTextInputLayout.setError(getResources().getString(R.string.mobile_number_error));
                doToast(getResources().getString(R.string.mobile_number_error));
                inputMobileOk = false;
                return;
            } else if (!ValidatorUtils.isValidMobileNo(mobileNoForNrc)) {
                mMobileNoForNrcTextInputLayout.setError(getResources().getString(R.string.invalid_mobile_number));
                doToast(getResources().getString(R.string.invalid_mobile_number));
                inputMobileOk = false;
                return;
            }
            if (inputMobileOk) {
                mMobileNoForNrcTextInputLayout.setErrorEnabled(false);
            }
        } else {
            boolean inputNrcOk = true;
            boolean inputMobileOk = true;

            if (TextUtils.isEmpty(passportNo)) {
                mPassportTextInputLayout.setError(getResources().getString(R.string.passport_error));
                doToast(getResources().getString(R.string.passport_error));
                inputNrcOk = false;
                return;
            } else if (!ValidatorUtils.isValidPassportNo(passportNo)) {
                mPassportTextInputLayout.setError(getResources().getString(R.string.invalid_passport_error));
                doToast(getResources().getString(R.string.invalid_passport_error));
                inputNrcOk = false;
                return;
            }

            if (inputNrcOk) {
                mPassportTextInputLayout.setErrorEnabled(false);
            }

            if (TextUtils.isEmpty(mobileNoForPassport)) {
                mMobileNoForPassportTextInputLayout.setError(getResources().getString(R.string.mobile_number_error));
                doToast(getResources().getString(R.string.mobile_number_error));
                inputMobileOk = false;
                return;
            }else if(mobileNoForPassport.length() < 6 || mobileNoForPassport.length() > 15){
                mMobileNoForPassportTextInputLayout.setError(getResources().getString(R.string.invalid_mobile_number));
                doToast(getResources().getString(R.string.invalid_mobile_number));
                inputMobileOk = false;
                return;
            }

            if (inputMobileOk) {
                mMobileNoForPassportTextInputLayout.setErrorEnabled(false);
            }
        }*/

        // prepare mobile number
        if (mobileNoForNrc.startsWith("09")) {
            mobileNoForNrc = mobileNoForNrc.substring(2, mobileNoForNrc.length());
        }
        mobileNoForNrc = "+959" + mobileNoForNrc;

        // input validation ok -- do saving
        final ParseUser user = new ParseUser();

        // Set standard fields
        //TODO username and email replace bcoz of avoiding duplicate
        user.setUsername(username);//username
        user.setPassword(password);

        //user.setEmail(email);

        user.put("username", username);
        user.put("phoneNo", mobileNoForNrc);
        //TODO:
       /* if (mDocType == DocType.NRC) {
            user.put("DocType", DocType.NRC.toString());
            user.put("NRC", nrcNo);
            user.put("Phone", mobileNoForNrc);
        } else {
            user.put("DocType", DocType.PASSPORT.toString());
            user.put("Passport", passportNo);
            user.put("Phone", mobileNoForPassport);
        }*/




        if(photoUri != null) {

           /* File photoFile = null;
            String photoUriString = photoUri.toString();
            if (photoUriString.startsWith("file://")) {
                photoFile = new File(photoUri.getPath());
            } else if (photoUriString.startsWith("content://")) {
                FileOutputStream photoOutputStream = null;
                InputStream contentInputStream = null;
                try {
                    Uri photoUri = Uri.parse(photoUriString);
                    photoFile = new File(
                            getTempPhotoStagingDirectory(),
                            URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8"));

                    photoOutputStream = new FileOutputStream(photoFile);
                    contentInputStream = getActivity()
                            .getContentResolver().openInputStream(photoUri);


                    Log.e("Sing up==","==>" + photoFile.getPath() + "/111119999" + photoFile.length());
                    byte[] buffer = new byte[512];
                    int len;
                    while ((len = contentInputStream.read(buffer)) > 0) {
                        photoOutputStream.write(buffer, 0, len);
                    }

                    Log.e("Sing up photofile","==" +buffer.length);
                    ParseFile ParsephotoFile = new ParseFile("photo.jpg", buffer);
                    user.put("user_profile_img", ParsephotoFile);

                } catch (FileNotFoundException fnfe) {
                    Log.e(TAG, "photo not found", fnfe);
                } catch (UnsupportedEncodingException uee) {
                    Log.e(TAG, "bad photo name", uee);
                } catch (IOException ioe) {
                    Log.e(TAG, "can't copy photo", ioe);
                } finally {
                    try {
                        if (photoOutputStream != null) {
                            photoOutputStream.close();
                        }
                        if (contentInputStream != null) {
                            contentInputStream.close();
                        }
                    } catch (IOException ioe) {
                        Log.e(TAG, "can't close streams");
                    }
                }
            }

            if (photoFile != null) {
                InputStream is = null;
                try {
                    is = new FileInputStream(photoFile);

                    // We only want to get the bounds of the image, rather than load the whole thing.
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(is, null, options);

                    *//*return new Pair<>(
                            photoFile, Math.min(options.outWidth, options.outHeight));*//*
                } catch (Exception e) {
                    *//*return null;*//*
                    e.printStackTrace();
                } finally {
                    Utility.closeQuietly(is);
                }
            }*/
            //content://com.android.providers.media.documents/document/image%3A32761
            File photoFile = null;
            String photoUriString = photoUri.toString();
            if (photoUriString.startsWith("file://")) {
                photoFile = new File(photoUri.getPath());
                Log.e("=PhotFIle==>","==>" + photoUriString);
            } else if (photoUriString.startsWith("content://")) {

                Log.e("===>","==>" + photoUriString);








            }
            Log.e("","" + photoUriString);

            FileInputStream fileInputStream = null;
            File file = new File(getPath(tempUri));
            if(file.exists()) {

                Log.e("Sing up file", "==>" + Uri.fromFile(file) +"/00/" + file.length());
                byte[] bFile = new byte[(int) file.length()];

                try {
                    //convert file into array of bytes
                    fileInputStream = new FileInputStream(file);
                    fileInputStream.read(bFile);
                    fileInputStream.close();

                    for (int i = 0; i < bFile.length; i++) {
                        //System.out.print((char)bFile[i]);
                    }

                    //System.out.println("Done");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ParseFile Excect","===>>" + e.toString() + file.getAbsolutePath() );
                }
/*                ParseFile ParsephotoFile = new ParseFile("photo.jpg", bFile);
                user.put("user_profile_img", ParsephotoFile);*/

            }
            else {
                Log.e("Sing up file", "//>" + Uri.parse(photoUri.getPath()));
            }


            /*FileInputStream fileInputStream = null;


            byte[] bFile = new byte[(int) photoFile.length()];

            try {
                //convert file into array of bytes
                fileInputStream = new FileInputStream(photoFile);
                fileInputStream.read(bFile);
                fileInputStream.close();

                for (int i = 0; i < bFile.length; i++) {
                    //System.out.print((char)bFile[i]);
                }

                //System.out.println("Done");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ParseFile Excect","===>>" + e.toString() + photoFile.getAbsolutePath() );
            }
            ParseFile ParsephotoFile = new ParseFile("photo.jpg", bFile);
            user.put("user_profile_img", ParsephotoFile);
*/


        }

        user.put("searchName", username.toLowerCase());

        //user.put("Address", "");//addressField.getText().toString());

        if (user_fb_id != null) {
            user.put("facebookId", user_fb_id);
        }
        if( user_fb_email != null){
            user.put("email",user_fb_email);
        }

        loadingStart();
        user.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {
                if (isActivityDestroyed()) {
                    return;
                }

                if (e == null) {
                    loadingFinish();
                    signupSuccess(user);
                } else {
                    loadingFinish();
                    if (e != null) {
                        debugLog(getString(R.string.com_parse_ui_login_warning_parse_signup_failed) +
                                e.toString());
                        switch (e.getCode()) {
                            case ParseException.INVALID_EMAIL_ADDRESS:
                                showToast(R.string.com_parse_ui_invalid_email_toast);
                                break;
                            case ParseException.USERNAME_TAKEN:
                                if(lang.equals(Utils.ENG_LANG)){
                                    showToast(R.string.com_parse_ui_username_taken_toast);
                                }
                                else if(lang.equals(Utils.MM_LANG)){

                                    Utils.doToastMM(mContext, getResources().getString(R.string.com_parse_ui_username_taken_toast_mm));
                                }


                                break;
                            case ParseException.EMAIL_TAKEN:
                                showToast(R.string.com_parse_ui_email_taken_toast);
                                break;
                            default:
                                if(lang.equals(Utils.ENG_LANG)){
                                    showToast(R.string.com_parse_ui_signup_failed_unknown_toast);
                                }
                                else if(lang.equals(Utils.MM_LANG)){

                                    Utils.doToastMM(mContext, getResources().getString(R.string.com_parse_ui_signup_failed_unknown_toast_mm));
                                }

                        }
                    }
                }
            }
        });

    }

    private File getTempPhotoStagingDirectory() {
        File photoDir = new File(getActivity().getCacheDir(), "photoFiles");
        photoDir.mkdirs();

        return photoDir;
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor =getActivity(). getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    private void signupSuccess(ParseUser user) {
        onLoginSuccessListener.onLoginSuccess(user);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode >= 0 ) {
            //listElements.get(requestCode).onActivityResult(data);
            if (tempUri != null) {
                photoUri = tempUri;
            } else if (data != null) {
                photoUri = data.getData();
            }
            setPhotoThumbnail();



        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        //callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

        //postStatusUpdateButton.setEnabled(enableButtons || canPresentShareDialog);
        //postPhotoButton.setEnabled(enableButtons || canPresentShareDialogWithPhotos);
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            user = me;
                            //updateUI();


                            //Toast.makeText(getApplicationContext(), "ID" + user.optString("id") + "Name" + user.optString("name") + "Email " +user.optString("email"), Toast.LENGTH_LONG).show();

                            if (user.optString("id") != null) {
                                //Toast.makeText(getActivity().getApplicationContext(), "Log In Successful." + user.optString("name") + user.optString("email").toString() + user.optString("id"), Toast.LENGTH_LONG).show();

                                usernameField.setText(user.optString("name"));
                                //emailField.setText(user.optString("email").toString());

                                user_fb_email = user.optString("email").toString();
                                user_fb_id = user.optString("id");
                                String fb_path = "https://graph.facebook.com/"+user_fb_id+"/picture";

                                /*Uri uri =  Uri.parse(fb_path  );
                                profile_rounded.setImageURI(uri);*/

                                try {
                                    register_profilePic_progressBar.setVisibility(View.VISIBLE);
                                    Picasso.with(mContext)
                                            .load(fb_path) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                                            .placeholder(R.drawable.camera_icon)
                                            .error(R.drawable.camera_icon)
                                            .into(profile_rounded, new ImageLoadedCallback(register_profilePic_progressBar) {
                                                @Override
                                                public void onSuccess() {
                                                    if (this.progressBar != null) {
                                                        this.progressBar.setVisibility(View.GONE);
                                                    } else {
                                                        this.progressBar.setVisibility(View.VISIBLE);
                                                    }
                                                }

                                            });
                                } catch (OutOfMemoryError outOfMemoryError) {
                                    outOfMemoryError.printStackTrace();
                                }



                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Please Log In", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            //parameters.putString(FIELDS, REQUEST_FIELDS);
            parameters.putString("fields", "id,name,email,gender, birthday");

            request.setParameters(parameters);
            GraphRequest.executeBatchAsync(request);
        } else {
            //Toast.makeText(getApplicationContext(), "accessToken Null Case ", Toast.LENGTH_LONG).show();

            user = null;
        }
    }

    public static boolean isNrcNoValid(String input) {
        Pattern pattern;
        Matcher matcher;
        final String NRC_PATTERN = "[0-9]{1,2}[/]([a-zA-Z]{3}){1,2}\\(N\\)[0-9]{6}"; //"([0-9]{1,2}[/][a-zA-Z]{3}\\(N\\))"; //"([\\u1000-\\u1021]{1,2})([0-9]{6})";
        pattern = Pattern.compile(NRC_PATTERN);
        matcher = pattern.matcher(input.toUpperCase());
        return matcher.matches();
    }

    public static String toCorrectFormat(String input) {
        String stateId = input.substring(input.indexOf('/') + 1, input.indexOf('('));
        String formattedStateId = "";

        if (stateId.length() == 3) {
            return input.toUpperCase();
        } else if (stateId.length() == 6) {
            char first = stateId.charAt(0);
            char second = stateId.charAt(2);
            char third = stateId.charAt(4);

            for (int i = 0; i < stateId.length(); i++) {
                char c = stateId.charAt(i);
                if (i == 0 || i == 2 || i == 4) {
                    formattedStateId += String.valueOf(c).toUpperCase();
                } else {
                    formattedStateId += String.valueOf(c).toLowerCase();
                }
            }
        }

        String formattedNrcNo = input.substring(0, input.indexOf('/') + 1) + formattedStateId + input.substring(input.indexOf('('), input.length());

        return formattedNrcNo;
    }

    private void doToast(String toast){
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }
    public void setEnglishFont(){
        img_upload_textview.setText(getResources().getString(R.string.com_parse_ui_parse_signup_img_upload_label_eng));
        usernameField.setHint(getResources().getString(R.string.com_parse_ui_name_input_hint));
        passwordField.setHint(getResources().getString(R.string.com_parse_ui_password_input_hint));
        confirmPasswordField.setHint(getResources().getString(R.string.com_parse_ui_confirm_password_input_hint));
        mTosTextView.setText(getResources().getString(R.string.i_agree));
        createAccountButton.setText(getResources().getString(R.string.com_parse_ui_create_account_button_label_eng));
        mobileNoForNrcField.setHint(getResources().getString(R.string.com_parse_ui_ph_input_hint));
        editAccountButton.setText(getResources().getString(R.string.com_parse_ui_edit_account_button_label_eng));
        //Set Type Face
        img_upload_textview.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        usernameField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        passwordField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        confirmPasswordField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        mTosTextView.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        createAccountButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
        editAccountButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
    }
    public void setMyanmarFont(){

        img_upload_textview.setText(getResources().getString(R.string.com_parse_ui_parse_signup_img_upload_label_mm));
        usernameField.setHint(getResources().getString(R.string.com_parse_ui_username_input_hint_mm));
        passwordField.setHint(getResources().getString(R.string.com_parse_ui_password_input_hint_mm));
        confirmPasswordField.setHint(getResources().getString(R.string.com_parse_ui_confirm_password_input_hint_mm));
        mobileNoForNrcField.setHint(getResources().getString(R.string.com_parse_ui_ph_input_hint_mm));
        createAccountButton.setText(getResources().getString(R.string.com_parse_ui_create_account_button_label_mm));
        editAccountButton.setText(getResources().getString(R.string.com_parse_ui_edit_account_button_label_mm));
        mTosTextView.setText(getResources().getString(R.string.i_agree_mm));

        //Set Type Face
        img_upload_textview.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        usernameField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        passwordField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        confirmPasswordField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        mTosTextView.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        createAccountButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
        editAccountButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));


    }

    private void showPhotoChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence camera = getResources().getString(R.string.action_photo_camera);
        CharSequence gallery = getResources().getString(R.string.action_photo_gallery);
        builder.setCancelable(true).
                setItems(new CharSequence[]{camera, gallery},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == CAMERA) {
                                    startCameraActivity();
                                } else if (i == GALLERY) {
                                    startGalleryActivity();
                                }
                            }
                        });
        builder.show();
    }
    private void setPhotoThumbnail() {
        // The selected image may be too big so scale here
        if (runningImageTask != null &&
                runningImageTask.getStatus() != AsyncTask.Status.FINISHED) {
            runningImageTask.cancel(true);
        }

        runningImageTask = new ScaleAndSetImageTask(photoUri);
        runningImageTask.execute();
    }

    private void startCameraActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        tempUri = getTempUri();
        if (tempUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        }
        startActivityForResult(intent, 0);
    }


    private void startGalleryActivity() {
        tempUri = null;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        String selectPicture = getResources().getString(R.string.select_picture);
        startActivityForResult(Intent.createChooser(intent, selectPicture), 0);
    }
    private Uri getTempUri() {
        String imgFileName = FILE_PREFIX + System.currentTimeMillis() + FILE_SUFFIX;

        // Note: on an emulator, you might need to create the "Pictures" directory in
        //         /mnt/sdcard first
        //       % adb shell
        //       % mkdir /mnt/sdcard/Pictures
        File image = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                imgFileName);
        return Uri.fromFile(image);
    }

   /* private void setPhotoText() {
        if (photoUri == null) {
            setText2(getResources().getString(R.string.action_photo_default));
        } else {
            setText2(getResources().getString(R.string.action_photo_ready));
        }
    }*/
    //Image Set Up
    private class ScaleAndSetImageTask extends AsyncTask<Void, Void, Bitmap> {
        private final Uri uri;

        public ScaleAndSetImageTask(Uri uri) {
            this.uri = uri;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        FacebookSdk.getApplicationContext().getContentResolver(), uri);
                if (bitmap.getHeight() > MAX_TEXTURE_SIZE || bitmap.getWidth() > MAX_TEXTURE_SIZE) {
                    // We need to scale the image
                    float scale = Math.min(
                            MAX_TEXTURE_SIZE / bitmap.getHeight(),
                            MAX_TEXTURE_SIZE / bitmap.getWidth());
                    Matrix matrix = new Matrix();
                    matrix.postScale(scale, scale);
                    bitmap = Bitmap.createBitmap(
                            bitmap,
                            0,
                            0,
                            bitmap.getWidth(),
                            bitmap.getHeight(),
                            matrix,
                            false);
                }
                return bitmap;
            } catch (Exception ex) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                profile_rounded.setImageBitmap(result);
            } else {
                // If we fail just try to set the image from the uri
                profile_rounded.setImageURI(photoUri);
            }
        }
    }

    private class ImageLoadedCallback implements com.squareup.picasso.Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progBar) {
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }
    //Get File Path
    /*private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }*/
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }




}
