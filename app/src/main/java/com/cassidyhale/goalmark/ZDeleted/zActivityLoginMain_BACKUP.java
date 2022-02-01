package com.cassidyhale.goalmark.ZDeleted;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cassidyhale.goalmark.Activities.ActivityHomescreen;
import com.cassidyhale.goalmark.Model.DataList;
import com.cassidyhale.goalmark.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

public class zActivityLoginMain_BACKUP extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button buttonSignIn;
    private Button buttonJustGo;
    private LoginButton buttonFacebook;
    private CallbackManager mCallbackManager;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_loginscreen_main);

        this.initializeViews();
        this.initialiazeFirebaseFunctionality();

    }

    private void initialiazeFirebaseFunctionality () {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // see if the user is SIGNED IN, OUT, CONNECTED, etc
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    Toast.makeText(zActivityLoginMain_BACKUP.this, "Signed In!", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(zActivityLoginMain_BACKUP.this, "Not Signed In!", Toast.LENGTH_LONG).show();
            }
        };
    }

    private void initializeViews () {
        buttonJustGo = findViewById(R.id.buttonJustGo);
        buttonJustGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoToHomeScr = new Intent (zActivityLoginMain_BACKUP.this, ActivityHomescreen.class);
                startActivity( intentGoToHomeScr);
            }
        });

//        emailField = (EditText) findViewById(R.id.editTextEmail);
//        passwordField = (EditText) findViewById(R.id.editTextPassword);
//
//        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if ( !TextUtils.isEmpty( emailField.getText().toString() ) &&
                         !TextUtils.isEmpty( passwordField.getText().toString()) )    {

                            String email = emailField.getText().toString().trim();
                            String password = passwordField.getText().toString().trim();
                            login (email, password);
                    }
                    else
                        Toast.makeText(zActivityLoginMain_BACKUP.this, "Incorrect!", Toast.LENGTH_LONG).show();
            }
        });
        // ----------------- FACEBOOK ----------------------------
        buttonFacebook = (LoginButton) findViewById(R.id.facebook_login_button);
        buttonFacebook.setReadPermissions("email", "public_profile");
        mCallbackManager = CallbackManager.Factory.create();

        buttonFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String userid = loginResult.getAccessToken().getUserId();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String first_name = object.getString( "first_name");
                            String last_name = object.getString( "last_name");
                            String email = object.getString( "email");
                            String id = object.getString( "id");
                            // Log.d("-------------> FACEBOOK", first_name +" "+ last_name +" "+ email +" "+ id + " ");

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, id" );
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
                handleFacebookAccessToken( loginResult.getAccessToken() );

                startActivity( new Intent (zActivityLoginMain_BACKUP.this, ActivityHomescreen.class));
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    }



    private void login (String email, String pwd)   {

        mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ) {
                    //Toast.makeText(ActivityLoginMain.this, "Signed In!", Toast.LENGTH_LONG).show();
                    startActivity( new Intent (zActivityLoginMain_BACKUP.this, ActivityHomescreen.class));
                    finish();
                    Log.d("login:","User is Signed in.");
                }
                else {
                    //Toast.makeText(ActivityLoginMain.this, "Failed Attempt!", Toast.LENGTH_LONG).show();
                    Log.d("login:","Failed to sign in!");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null)
            mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mUser = mAuth.getCurrentUser();
                            try {
                                boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                                if(isNewUser){
                                    Log.d("USER","-------------- NEW User comming from FACEBBOOK --------------");
                                    String userId = mAuth.getCurrentUser().getUid();
                                    DataList newDataListUser = new DataList(userId);

                                } else
                                    Log.d("USER","-------------- OLD User comming from FACEBBOOK --------------");

                            } catch (NullPointerException e){
                                Log.d ("Firebase","No User.");
                                e.printStackTrace();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(zActivityLoginMain_BACKUP.this, "FB Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}