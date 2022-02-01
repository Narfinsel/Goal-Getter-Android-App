package com.cassidyhale.goalmark.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLoginMain extends AppCompatActivity {

    private Button buttonJustGo;
    private LoginButton buttonFacebook;
    private CallbackManager mCallbackManager;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_loginscreen_main);

        buttonJustGo = findViewById(R.id.buttonJustGo);
        buttonJustGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivityHomescreen();
            }
        });

        this.initialize_Firebase_Functionality();
        this.initialize_SignIn_Facebook();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn)
            this.switchActivityHomescreen();
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

    private void switchActivityHomescreen ()    {
        startActivity( new Intent (ActivityLoginMain.this, ActivityHomescreen.class));
        finish();
    }

    private void initialize_Firebase_Functionality () {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // see if the user is SIGNED IN, OUT, CONNECTED, etc
                mUser = firebaseAuth.getCurrentUser();
//                if (mUser != null) {
//                    Toast.makeText(ActivityLoginMain.this, "Signed In!", Toast.LENGTH_LONG).show();
//                }
//                else
//                    Toast.makeText(ActivityLoginMain.this, "Not Signed In!", Toast.LENGTH_LONG).show();
            }
        };
    }

    private void initialize_SignIn_Facebook () {
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
                        mUser = mAuth.getCurrentUser();
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
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Log.d("USERx", "LOGINscreen -- signInWithCredential: " + task.isSuccessful());

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            if (task.getResult() != null)   {
                                if (task.getResult().getUser() != null) {
                                    mUser = task.getResult().getUser();
                                    if (mUser!=null) {
                                        // mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(mUser.getUid());
                                        // Log.d("USERx", "LOGINscreen -- buttonFace : registerCallback : onSuccess:    " + mUser.getUid());
                                        switchActivityHomescreen();
                                    }
                                }
                                boolean isUserNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            }
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ActivityLoginMain.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }


}