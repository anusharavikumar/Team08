package com.cmpe277.skibuddy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private static GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Sign In";
    private static final int signInRequestCode = 9001;
    private static GoogleSignInAccount acct;
    private GoogleSignInResult result;
    private ImageView profilepic;
    private TextView name;
    private TextView email;
    private Button continuebutton;


    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static String facebookurl;
    private static Boolean isFacebook = false;
    private static String googlename;
    private static String googleemail;
    private static String facebookname;
    private static String facebookemail;
    private static String googleuserid;
    private static String facebookuserid;
    private static String url;

    public static GoogleApiClient getGoogleAPIclient(){
        return mGoogleApiClient;
    }

    public static String getUserEmail(){
        if(isFacebook){
            return facebookemail;
        }else{
            return googleemail;
        }
    }
    public static String getUserAcctId(){
        if(isFacebook){
            return facebookuserid;
        }else{
            return googleuserid;
        }
    }
    public static String getname(){
        if(isFacebook){
            return facebookname;
        }else{
            return googlename;
        }
    }
    public static String getURL(){
        return url;
    }
    /*public static String geturl(){
        if(url == null){
            return facebookurl;
        }else{
            return url;
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FACEBOOK
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        continuebutton = (Button)findViewById(R.id.buttonContinue);

        /*if (!isFacebook || !mGoogleApiClient.isConnected()){
            getSupportActionBar().hide();
        }else{
            getSupportActionBar().show();
        }*/

        //this.getActionBar().show();
        //GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .requestProfile()
                .requestScopes(Plus.SCOPE_PLUS_PROFILE)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        profilepic = (ImageView) findViewById(R.id.profilepic);
        name = (TextView) findViewById(R.id.nameid);
        email = (TextView) findViewById(R.id.emailid);


        profilepic.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        continuebutton.setVisibility(View.GONE);

        //FACEBOOK
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("user_likes", "user_status", "email", "public_profile"));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        isFacebook = true;
                        String FacebookUserID = loginResult.getAccessToken().getUserId();
                        facebookuserid = FacebookUserID;
                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                    /* handle the result */
                                        Log.v("LoginActivity", response.toString());
                                        try {
                                            response.getJSONObject().getString("name");
                                            facebookname = response.getJSONObject().getString("name");
                                            name.setText(facebookname);
                                            name.setVisibility(View.VISIBLE);
                                        } catch (JSONException j) {
                                            Log.d("Facebook", j.getLocalizedMessage());
                                        }
                                    }
                                }
                        ).executeAsync();

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.i("LoginActivityfinal", response.toString());
                                        Bundle bFacebookData = getFacebookData(object);
                                        facebookemail = bFacebookData.getString("email");
                                    }
                                }
                        );
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email, gender"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();
                        //updateUI(true);
                        //facebookurl = "https://graph.facebook.com/" + FacebookUserID + "/picture?type=large";
                        url = "https://graph.facebook.com/" + facebookuserid + "/picture?type=large";
                        new getProfileURLAndSetImageView(profilepic, FacebookUserID, true).execute();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    private Bundle getFacebookData(JSONObject object) {
        String id;
        Bundle bundle = null;
        try {
            bundle = new Bundle();
            id = object.getString("id");
            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            return bundle;
        } catch (JSONException e){
            Log.d("Facebook", e.getLocalizedMessage());
        }
        return bundle;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == signInRequestCode) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        //Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            acct = result.getSignInAccount();
            //String Stringurl = "https://www.googleapis.com/plus/v1/people/" + acct.getId() + "?fields=image&key=AIzaSyCkDrLVCePMsRyRg6JlNo1tBVX6wqygB8s";
            googleuserid = acct.getId();
            googleemail = acct.getEmail();
            googlename = acct.getDisplayName();
            url = "https://www.googleapis.com/plus/v1/people/" + googleuserid + "?fields=image&key=AIzaSyCkDrLVCePMsRyRg6JlNo1tBVX6wqygB8s";

            if (googleuserid != null) {
                new getProfileURLAndSetImageView(profilepic, googleuserid, false).execute();
            }
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, signInRequestCode);
    }

    private void signOutGoogle() {
        mGoogleApiClient.connect();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccessGoogle() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void updateUI(boolean signedIn) {
        if (!signedIn) {
            getSupportActionBar().hide();
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.login_button).setVisibility(View.VISIBLE);
            profilepic.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.homeImage);
            linearLayout.setBackground(getResources().getDrawable(R.drawable.androidwallpaper, null));
            continuebutton.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //if (isFacebook || mGoogleApiClient.isConnected()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        //}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_signout) {
            if (isFacebook){
                isFacebook= false;
                LoginManager.getInstance().logOut();
                updateUI(false);
            }
            else {
                signOutGoogle();
           }
            return true;
        }
        if(id == R.id.action_signoutanddisconnect){
            if (isFacebook){
                isFacebook= false;
                LoginManager.getInstance().logOut();
                updateUI(false);
            }else {
                revokeAccessGoogle();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class getProfileURLAndSetImageView extends AsyncTask<String, Void, Bitmap> {

        String inputurl;
        Boolean isFacebook;
        String url2;
        URL imageUrl;
        ImageView i;
        Bitmap b;
        //String facebookurl = "https://graph.facebook.com/" + inputurl + "/picture?type=large";
        //String Stringurl = "https://www.googleapis.com/plus/v1/people/" + acct.getId() + "?fields=image&key=AIzaSyCkDrLVCePMsRyRg6JlNo1tBVX6wqygB8s";


        public getProfileURLAndSetImageView(ImageView profileimage, String urlString, Boolean isFacebook) {

            if (isFacebook){
                this.inputurl = "https://graph.facebook.com/" + urlString + "/picture?type=large";
                url = "https://graph.facebook.com/" + urlString + "/picture?type=large";
            }else {
                this.inputurl = "https://www.googleapis.com/plus/v1/people/" + urlString + "?fields=image&key=AIzaSyCkDrLVCePMsRyRg6JlNo1tBVX6wqygB8s";
                url = "https://www.googleapis.com/plus/v1/people/" + urlString + "?fields=image&key=AIzaSyCkDrLVCePMsRyRg6JlNo1tBVX6wqygB8s";
            }


            this.isFacebook = isFacebook;
            this.i = profileimage;
        }

        protected Bitmap doInBackground(String... urls) {

            if (!isFacebook) {
                StringBuilder serverSB = new StringBuilder();
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(inputurl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setUseCaches(false);
                    urlConnection.setConnectTimeout(10000);
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.i("Google Profile Pic", "Failed : HTTP error code : " + urlConnection.getResponseCode());
                    } else {
                        BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
                        String serverOutput;
                        System.out.println("Output from GET .... \n");
                        //To ensure the body is not zero
                        while ((serverOutput = br.readLine()) != null) {
                            System.out.println(serverOutput);
                            serverSB.append(serverOutput);
                        }
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(serverSB.toString());
                            JSONObject queryArray = jsonObject.getJSONObject("image");
                            url2 = queryArray.get("url").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException a) {
                    a.printStackTrace();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
                //System.out.println("GOOGLE"+url2);
                try{
                    url = url2;
                    imageUrl = new URL(url2);
                }catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try{
                    imageUrl = new URL(inputurl);
                }catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }

            try{
                b = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            }catch (IOException e){
                e.printStackTrace();
            }
            return b;
        }

        protected void onPostExecute(Bitmap result) {
            getSupportActionBar().show();
            i.setImageBitmap(result);
            i.setTag(imageUrl.toString());
            i.setVisibility(View.VISIBLE);
            continuebutton.setVisibility(View.VISIBLE);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.homeImage);
            linearLayout.setBackgroundColor(Color.TRANSPARENT);
            if (isFacebook){
                email.setText("Signed In as: \n" + facebookemail);
                name.setText("Welcome: \n" +facebookname);
                Toast.makeText(getApplicationContext(), "Logged in via Facebook: Welcome " + facebookname + ", " + facebookemail, Toast.LENGTH_SHORT).show();
            }else{
                name.setText("Welcome: \n" + acct.getDisplayName());
                email.setText("Signed In as: \n" + acct.getEmail());
                Toast.makeText(getApplicationContext(), "Logged in via Google: Welcome " + googlename + ", " + googleemail, Toast.LENGTH_SHORT).show();
            }
            name.setTextSize(18);
            email.setTextSize(18);

            email.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            findViewById(R.id.login_button).setVisibility(View.GONE);
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);


            AsyncHttpClient client = new AsyncHttpClient();
            try {
                StringEntity entity = new StringEntity("{'data': [{'user_id':'" + getUserEmail() + "', 'name':'" + getname() + "', 'photo_url':'" + getURL() + "'}]}");

                client.post(getApplicationContext(), "http://52.91.8.130:8000/checkUser/", entity, "application/json",
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Log.d("Response", "" + response);
                            }
                        });
            }
            catch(UnsupportedEncodingException e) {
                    e.printStackTrace();
            }


            continuebutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), SignedInUi.class);
                    i.putExtra("name", getname());
                    i.putExtra("email", getUserEmail());
                    i.putExtra("url", imageUrl.toString());
                    startActivity(i);
                }
            });
        }
    }
}

