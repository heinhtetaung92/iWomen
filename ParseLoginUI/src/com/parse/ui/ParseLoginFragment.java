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
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.model.MyTypeFace;
import com.parse.utils.Utils;


/**
 * Fragment for the user login screen.
 */
public class ParseLoginFragment extends ParseLoginFragmentBase implements CompoundButton.OnCheckedChangeListener{



  public interface ParseLoginFragmentListener {
    public void onSignUpClicked(String username, String password);

    public void onLoginHelpClicked();

    public void onLoginSuccess();
  }

  private static final String LOG_TAG = "ParseLoginFragment";
  private static final String USER_OBJECT_NAME_FIELD = "username";
    private static final String USER_OBJECT_EMAIL_FIELD = "email";

  private View parseLogin;
  private ImageView appLogo;
  private EditText usernameField;
  private EditText passwordField;
  private TextView loginMotoTextView;
  private TextView parseLoginHelpButton;
  private Button parseLoginButton;
  private Button parseSignupButton;
  private Button facebookLoginButton;
  private Button twitterLoginButton;
  private ParseLoginFragmentListener loginFragmentListener;
  private ParseOnLoginSuccessListener onLoginSuccessListener;

  private ParseLoginConfig config;

  /*
   * ****Font Setting*********/
  RadioButton rd_lang_en, rd_lang_mm;
  SharedPreferences sharePref;

  public static ParseLoginFragment newInstance(Bundle configOptions) {
    ParseLoginFragment loginFragment = new ParseLoginFragment();
    loginFragment.setArguments(configOptions);
    return loginFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                           Bundle savedInstanceState) {
    config = ParseLoginConfig.fromBundle(getArguments(), getActivity());

    View v = inflater.inflate(R.layout.com_parse_ui_parse_login_fragment,
        parent, false);
    appLogo= (ImageView) v.findViewById(R.id.app_logo);
    parseLogin = v.findViewById(R.id.parse_login);
    usernameField = (EditText) v.findViewById(R.id.login_username_input);
    passwordField = (EditText) v.findViewById(R.id.login_password_input);
    loginMotoTextView = (TextView) v.findViewById(R.id.login_moto_textview);
    parseLoginHelpButton = (Button) v.findViewById(R.id.parse_login_help);
    parseLoginButton = (Button) v.findViewById(R.id.parse_login_button);
    parseSignupButton = (Button) v.findViewById(R.id.parse_signup_button);
    facebookLoginButton = (Button) v.findViewById(R.id.facebook_login);
    twitterLoginButton = (Button) v.findViewById(R.id.twitter_login);

    /******Font Setting*********/
    sharePref = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);
    rd_lang_en = (RadioButton) v.findViewById(R.id.settings_english_language);
    rd_lang_mm = (RadioButton) v.findViewById(R.id.settings_myanmar_language);

    String lang = sharePref.getString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);
    rd_lang_en.setOnCheckedChangeListener(this);
    rd_lang_mm.setOnCheckedChangeListener(this);
    if(lang.equals(Utils.ENG_LANG)){
      rd_lang_en.setChecked(true);
      setEnglishFont();
    }
    else if(lang.equals(Utils.MM_LANG)){
      rd_lang_mm.setChecked(true);
      setMyanmarFont();
    }
    /******Font Setting*********/



    if (appLogo != null && config.getAppLogo() != null) {
      appLogo.setImageResource(config.getAppLogo());
    }
    if (allowParseLoginAndSignup()) {
      setUpParseLoginAndSignup();
    }
    if (allowFacebookLogin()) {
      setUpFacebookLogin();
    }
    if (allowTwitterLogin()) {
      setUpTwitterLogin();
    }

    //loginMotoTextView.setVisibility(View.VISIBLE);

    return v;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    if (activity instanceof ParseLoginFragmentListener) {
      loginFragmentListener = (ParseLoginFragmentListener) activity;
    } else {
      throw new IllegalArgumentException(
          "Activity must implemement ParseLoginFragmentListener");
    }

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

  @Override
  protected String getLogTag() {
    return LOG_TAG;
  }

  private void setUpParseLoginAndSignup() {
    parseLogin.setVisibility(View.VISIBLE);

    if (config.isParseLoginEmailAsUsername()) {
      //usernameField.setHint(R.string.com_parse_ui_email_input_hint);
      usernameField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    if (config.getParseLoginButtonText() != null) {
      parseLoginButton.setText(config.getParseLoginButtonText());
    }

    parseLoginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (username.length() == 0) {
          if (config.isParseLoginEmailAsUsername()) {
            showToast(R.string.com_parse_ui_no_email_toast);
          } else {
            showToast(R.string.com_parse_ui_no_username_toast);
          }
        } else if (password.length() == 0) {
          showToast(R.string.com_parse_ui_no_password_toast);
        } else {
          loadingStart(true);
          ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
              if (isActivityDestroyed()) {
                return;
              }

              if (user != null) {
                loadingFinish();

                loginSuccess(user);
              } else {
                loadingFinish();
                if (e != null) {
                  debugLog(getString(R.string.com_parse_ui_login_warning_parse_login_failed) +
                      e.toString());
                  if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                    if (config.getParseLoginInvalidCredentialsToastText() != null) {
                      showToast(config.getParseLoginInvalidCredentialsToastText());
                    } else {
                      showToast(R.string.com_parse_ui_parse_login_invalid_credentials_toast);
                    }
                    passwordField.selectAll();
                    passwordField.requestFocus();
                  } else {
                    showToast(R.string.com_parse_ui_parse_login_failed_unknown_toast);
                  }
                }
              }
            }
          });
        }
      }
    });

    if (config.getParseSignupButtonText() != null) {
      parseSignupButton.setText(config.getParseSignupButtonText());
    }

    parseSignupButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        loginFragmentListener.onSignUpClicked(username, password);
      }
    });

    if (config.getParseLoginHelpText() != null) {
      parseLoginHelpButton.setText(config.getParseLoginHelpText());
    }

    parseLoginHelpButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        loginFragmentListener.onLoginHelpClicked();
      }
    });
  }

  private void setUpFacebookLogin() {
    facebookLoginButton.setVisibility(View.VISIBLE);

    if (config.getFacebookLoginButtonText() != null) {
      facebookLoginButton.setText(config.getFacebookLoginButtonText());
    }

    facebookLoginButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        loadingStart(true);
        ParseFacebookUtils.logIn(config.getFacebookLoginPermissions(),
            getActivity(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (isActivityDestroyed()) {
              return;
            }

            if (user == null) {

              loadingFinish();
                //loginSuccess(user);
              if (e != null) {
                showToast(R.string.com_parse_ui_facebook_login_failed_toast);
                debugLog(getString(R.string.com_parse_ui_login_warning_facebook_login_failed) +
                    e.toString());
              }
            } else if (user.isNew()) {
              /*Request.newMeRequest(ParseFacebookUtils.getSession(),
                  new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser fbUser,
                                            Response response) {
                      *//*
                        If we were able to successfully retrieve the Facebook
                        user's name, let's set it on the fullName field.
                      *//*
                      final ParseUser parseUser = ParseUser.getCurrentUser();
                      if (fbUser != null && parseUser != null
                          && fbUser.getName().length() > 0) {
                        parseUser.put(USER_OBJECT_NAME_FIELD, fbUser.getName());
                        parseUser.put(USER_OBJECT_EMAIL_FIELD,fbUser.getProperty("email").toString());
                        showToast("User Email" +fbUser.getProperty("email").toString() );
                        //Log.e("////////////FB email/////////////" ,"Email" + fbUser.getProperty("email").toString());
                        parseUser.saveInBackground(new SaveCallback() {
                          @Override
                          public void done(ParseException e) {
                            if (e != null) {
                              debugLog(getString(
                                  R.string.com_parse_ui_login_warning_facebook_login_user_update_failed) +
                                  e.toString());
                            }
                            loginSuccess(parseUser);
                          }
                        });
                      }
                      loginSuccess(parseUser);
                    }
                  }
              ).executeAsync();*/
            } else {
              loginSuccess(user);
            }
          }
        });
          //which will save any changes to the session token back to the ParseUser and ensure that this session data follows the user wherever it logs in.
          //ParseUser parseUser = ParseUser.getCurrentUser();

          //ParseFacebookUtils.saveLatestSessionDataInBackground(parseUser);
      }
    });
  }

  private void setUpTwitterLogin() {
    twitterLoginButton.setVisibility(View.VISIBLE);

    if (config.getTwitterLoginButtonText() != null) {
      twitterLoginButton.setText(config.getTwitterLoginButtonText());
    }

    /*twitterLoginButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        loadingStart(false); // Twitter login pop-up already has a spinner
        ParseTwitterUtils.logIn(getActivity(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (isActivityDestroyed()) {
              return;
            }

            if (user == null) {
              loadingFinish();
              if (e != null) {
                showToast(R.string.com_parse_ui_twitter_login_failed_toast);
                debugLog(getString(R.string.com_parse_ui_login_warning_twitter_login_failed) +
                    e.toString());
              }
            } else if (user.isNew()) {
              Twitter twitterUser = ParseTwitterUtils.getTwitter();
              if (twitterUser != null
                  && twitterUser.getScreenName().length() > 0) {
                *//*
                  To keep this example simple, we put the users' Twitter screen name
                  into the name field of the Parse user object. If you want the user's
                  real name instead, you can implement additional calls to the
                  Twitter API to fetch it.
                *//*
                user.put(USER_OBJECT_NAME_FIELD, twitterUser.getScreenName());
                user.saveInBackground(new SaveCallback() {
                  @Override
                  public void done(ParseException e) {
                    if (e != null) {
                      debugLog(getString(
                          R.string.com_parse_ui_login_warning_twitter_login_user_update_failed) +
                          e.toString());
                    }
                    loginSuccess();
                  }
                });
              }
            } else {
              loginSuccess();
            }
          }
        });
      }
    });*/
  }

  private boolean allowParseLoginAndSignup() {
    if (!config.isParseLoginEnabled()) {
      return false;
    }

    if (usernameField == null) {
      debugLog(R.string.com_parse_ui_login_warning_layout_missing_username_field);
    }
    if (passwordField == null) {
      debugLog(R.string.com_parse_ui_login_warning_layout_missing_password_field);
    }
    if (parseLoginButton == null) {
      debugLog(R.string.com_parse_ui_login_warning_layout_missing_login_button);
    }
    if (parseSignupButton == null) {
      debugLog(R.string.com_parse_ui_login_warning_layout_missing_signup_button);
    }
    if (parseLoginHelpButton == null) {
      debugLog(R.string.com_parse_ui_login_warning_layout_missing_login_help_button);
    }

    boolean result = (usernameField != null) && (passwordField != null)
        && (parseLoginButton != null) && (parseSignupButton != null)
        && (parseLoginHelpButton != null);

    if (!result) {
      debugLog(R.string.com_parse_ui_login_warning_disabled_username_password_login);
    }
    return result;
  }

  private boolean allowFacebookLogin() {
    if (!config.isFacebookLoginEnabled()) {
      return false;
    }

    if (facebookLoginButton == null) {
      debugLog(R.string.com_parse_ui_login_warning_disabled_facebook_login);
      return false;
    } else {
      return true;
    }
  }

  private boolean allowTwitterLogin() {
    if (!config.isTwitterLoginEnabled()) {
      return false;
    }

    if (twitterLoginButton == null) {
      debugLog(R.string.com_parse_ui_login_warning_disabled_twitter_login);
      return false;
    } else {
      return true;
    }
  }

  private void loginSuccess(ParseUser user) {
    onLoginSuccessListener.onLoginSuccess(user);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    SharedPreferences.Editor editor = sharePref.edit();


    if(isChecked) {
      if (buttonView.getId() == R.id.settings_english_language) {

        editor.putString(Utils.PREF_SETTING_LANG, Utils.ENG_LANG);

        setEnglishFont();


      } else if (buttonView.getId() == R.id.settings_myanmar_language) {
        editor.putString(Utils.PREF_SETTING_LANG, Utils.MM_LANG);

        setMyanmarFont();

      }

    }
    editor.commit();

  }

  public void setEnglishFont(){
    appLogo.setImageResource(R.drawable.logo_eng);
    usernameField.setHint(getResources().getString(R.string.com_parse_ui_name_input_hint));
    passwordField.setHint(getResources().getString(R.string.com_parse_ui_password_input_hint));
    parseLoginButton.setText(getResources().getString(R.string.com_parse_ui_parse_login_button_label));
    parseLoginHelpButton.setText(getResources().getString(R.string.com_parse_ui_forgot_password_button_label));

    parseSignupButton.setText(getResources().getString(R.string.com_parse_ui_create_account_button_label));

    //Set Type Face
    usernameField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
    passwordField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
    parseLoginButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
    parseLoginHelpButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));
    parseSignupButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.NORMAL));

  }
  public void setMyanmarFont(){
    appLogo.setImageResource(R.drawable.logo_mm);
    usernameField.setHint(getResources().getString(R.string.com_parse_ui_username_input_hint_mm));
    passwordField.setHint(getResources().getString(R.string.com_parse_ui_password_input_hint_mm));
    parseLoginButton.setText(getResources().getString(R.string.com_parse_ui_parse_login_button_label_mm));
    parseLoginHelpButton.setText(getResources().getString(R.string.com_parse_ui_forgot_password_button_mm));

    parseSignupButton.setText(getResources().getString(R.string.com_parse_ui_parse_signup_button_label_mm));

    //Set Type Face
    usernameField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
    passwordField.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
    parseLoginButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
    parseLoginHelpButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));
    parseSignupButton.setTypeface(MyTypeFace.get(getActivity().getApplicationContext(), MyTypeFace.ZAWGYI));

  }

}
