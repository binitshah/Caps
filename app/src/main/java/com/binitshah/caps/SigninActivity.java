package com.binitshah.caps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SigninActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "CAPSDEBUG";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth; //firebase
    private FirebaseAuth.AuthStateListener mAuthListener; //firebase
    private GoogleApiClient mGoogleApiClient; //google
    private CallbackManager mCallbackManager; //fb
    private LoginButton fbloginButton; //fb

    private Button loginButton;
    private Button signupButton;
    private EditText emailEdittext;
    private EditText passwordEdittext;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signin);

        // ENTERING THE REALM OF FIREBASE //

        mAuth = FirebaseAuth.getInstance(); //this is the instance of firebase authentication that will be intantiated for use
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    try{
                        String welcome = getResources().getString(R.string.welcome_message);
                        String welcomemessage = welcome + " " + user.getDisplayName() + "!";
                        Toast.makeText(SigninActivity.this, welcomemessage, Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException exception){
                        Log.w(TAG, "NullPointerException: " + exception);
                    }
                    Log.d(TAG, "Firebase onAuthStateChanged: signed IN" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "Firebase onAuthStateChanged: signed OUT");
                }
            }
        };

        //ENTERING THE REALM OF FACEBOOK //

        fbloginButton = (LoginButton) findViewById(R.id.facebooksignin_button);
        fbloginButton.setVisibility(View.GONE);
        fbloginButton.setReadPermissions("email", "public_profile");
        mCallbackManager = CallbackManager.Factory.create();
        fbloginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(SigninActivity.this, "Facebook login cancelled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(SigninActivity.this, "Facebook login error.", Toast.LENGTH_SHORT).show();
            }
        });
        Button fbButton = (Button) findViewById(R.id.fbbutton);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbloginButton.performClick();
            }
        });


        // ENTERING THE REALM OF GOOOOOOOOGLE //

        Button gButton = (Button) findViewById(R.id.gbutton);
        gButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //begins sign in process
                Log.d(TAG, "OFFICIAL GOOGLE SIGN IN BUTTON CLICKED");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_idd))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // ENTERING THE REALM OF EMAIL/PASSWORD //

        emailEdittext = (EditText) findViewById(R.id.email_edittext);
        passwordEdittext = (EditText) findViewById(R.id.password_edittext);
        signupButton = (Button) findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatedFields()){
                    Toast.makeText(SigninActivity.this, "validated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginButton = (Button) findViewById(R.id.login);
    }

    //GOOGLE & FACEBOOK
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data); //facebook part

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Successful Google Sign In
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Failed Google Sign In
                String errorUnableToSignIn = getResources().getString(R.string.unabletosignin_error);
                Toast.makeText(SigninActivity.this, errorUnableToSignIn, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //FACEBOOK
    private void firebaseAuthWithFacebook(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException()); //todo remove at release
                            String errorUnableToSignIn = getResources().getString(R.string.unabletosignin_error);
                            Toast.makeText(SigninActivity.this, errorUnableToSignIn, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            finishSignin();
                        }
                    }
                });
    }

    //GOOGLE
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException()); //todo remove at release
                            String errorUnableToSignIn = getResources().getString(R.string.unabletosignin_error);
                            Toast.makeText(SigninActivity.this, errorUnableToSignIn, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            finishSignin();
                        }
                    }
                });
    }

    // EVERYTHING BELOW THIS IS GENERAL //

    public boolean validatedFields(){
        if(emailEdittext.getText().toString().equals("")){
            String emptyEmail = getResources().getString(R.string.email_required_error);
            emailEdittext.setError(emptyEmail);
            return false;
        }
        if(!emailEdittext.getText().toString().contains("@")){
            String emailNotValidError = getResources().getString(R.string.email_notvalid_error);
            emailEdittext.setError(emailNotValidError);
            return false;
        }
        if(passwordEdittext.getText().toString().equals("")){
            String emptypassword = getResources().getString(R.string.password_required_error);
            passwordEdittext.setError(emptypassword);
            return false;
        }
        if(passwordEdittext.getText().toString().length() < 6){
            String shortpassword = getResources().getString(R.string.password_tooshort_error);
            passwordEdittext.setError(shortpassword);
            return false;
        }
        return true;
    }

    public void finishSignin(){
        SharedPreferences.Editor editor = getSharedPreferences("CAPS", MODE_PRIVATE).edit();
        editor.putBoolean("login", true);
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        String googleServiceErrorMessage = getResources().getString(R.string.googleservice_error);
        Toast.makeText(this, googleServiceErrorMessage, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            String progressmessage = getResources().getString(R.string.progress_dialog);
            mProgressDialog.setMessage(progressmessage);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}